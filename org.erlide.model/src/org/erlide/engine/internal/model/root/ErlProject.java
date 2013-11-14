/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.erlide.engine.internal.model.root;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IPathVariableManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.erlide.engine.ErlangEngine;
import org.erlide.engine.internal.ModelPlugin;
import org.erlide.engine.internal.model.cache.ErlModelCache;
import org.erlide.engine.internal.model.erlang.ErlExternalReferenceEntryList;
import org.erlide.engine.internal.model.erlang.ErlOtpExternalReferenceEntryList;
import org.erlide.engine.internal.util.ModelConfig;
import org.erlide.engine.model.ErlModelException;
import org.erlide.engine.model.ErlModelStatus;
import org.erlide.engine.model.ErlModelStatusConstants;
import org.erlide.engine.model.IErlModel;
import org.erlide.engine.model.IErlModelMarker;
import org.erlide.engine.model.IOpenable;
import org.erlide.engine.model.builder.BuilderConfig;
import org.erlide.engine.model.builder.BuilderTool;
import org.erlide.engine.model.builder.ErlangBuilder;
import org.erlide.engine.model.erlang.IErlModule;
import org.erlide.engine.model.erlang.ModuleKind;
import org.erlide.engine.model.root.ErlElementKind;
import org.erlide.engine.model.root.ErlangProjectProperties;
import org.erlide.engine.model.root.IErlElement;
import org.erlide.engine.model.root.IErlElementLocator;
import org.erlide.engine.model.root.IErlElementVisitor;
import org.erlide.engine.model.root.IErlExternal;
import org.erlide.engine.model.root.IErlExternalRoot;
import org.erlide.engine.model.root.IErlFolder;
import org.erlide.engine.model.root.IErlProject;
import org.erlide.engine.model.root.ProjectConfigurationChangeListener;
import org.erlide.engine.model.root.ProjectConfigurationPersister;
import org.erlide.engine.services.search.OpenService;
import org.erlide.engine.util.CommonUtils;
import org.erlide.engine.util.NatureUtil;
import org.erlide.engine.util.SourcePathUtils;
import org.erlide.runtime.runtimeinfo.RuntimeInfo;
import org.erlide.runtime.runtimeinfo.RuntimeVersion;
import org.erlide.util.ErlLogger;
import org.erlide.util.PreferencesUtils;
import org.osgi.service.prefs.BackingStoreException;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

/**
 * Handle for an Erlang Project.
 * 
 * <p>
 * A Erlang Project internally maintains a devpath that corresponds to the
 * project's classpath. The classpath may include source folders from the
 * current project; jars in the current project, other projects, and the local
 * file system; and binary folders (output location) of other projects. The
 * Erlang Model presents source elements corresponding to output .class files in
 * other projects, and thus uses the devpath rather than the classpath (which is
 * really a compilation path). The devpath mimics the classpath, except has
 * source folder entries in place of output locations in external projects.
 * 
 * <p>
 * Each ErlProject has a NameLookup facility that locates elements on by name,
 * based on the devpath.
 * 
 * @see IErlProject
 */
public class ErlProject extends Openable implements IErlProject,
        ProjectConfigurationChangeListener {

    /**
     * Whether the underlying file system is case sensitive.
     */
    private static final boolean IS_CASE_SENSITIVE = !new File("Temp").equals(new File("temp")); //$NON-NLS-1$ //$NON-NLS-2$

    protected IProject fProject;
    private ErlangProjectProperties properties;
    private Collection<IResource> nonErlangResources;
    private BuilderTool builderTool;
    private BuilderConfig builderConfig;

    public ErlProject(final IProject project, final ErlElement parent) {
        super(parent, project.getName());
        fProject = project;
        nonErlangResources = null;
    }

    /**
     * @see Openable
     */
    @Override
    public boolean buildStructure(final IProgressMonitor pm)
            throws ErlModelException {
        final IResource r = getCorrespondingResource();
        // check whether the Erlang project can be opened
        if (!(r instanceof IContainer) || !r.isAccessible()) {
            ErlLogger.warn(
                    "Project %s has no resources: res:%s acc:%s cont:%s",
                    getName(), r, r == null ? "?" : r.isAccessible(),
                    r instanceof IContainer);
            throw new ErlModelException(new ErlModelStatus(
                    ErlModelStatusConstants.ELEMENT_DOES_NOT_EXIST, this));
        }
        try {
            final IContainer c = (IContainer) r;
            final IResource[] elems = c.members();
            final List<IErlElement> children = new ArrayList<IErlElement>(
                    elems.length + 1);
            // ErlLogger.debug(">>adding externals");
            addExternals(children);
            // ErlLogger.debug("childcount %d", children.size());
            // ErlLogger.debug(">>adding otp");
            addOtpExternals(children);
            // ErlLogger.debug("childcount %d", children.size());
            final IErlModel model = ErlangEngine.getInstance().getModel();
            for (final IResource element : elems) {
                if (element instanceof IFolder) {
                    final IFolder folder = (IFolder) element;
                    final IErlFolder erlFolder = (IErlFolder) model
                            .create(folder);
                    children.add(erlFolder);
                } else if (element instanceof IFile) {
                    final IFile file = (IFile) element;
                    if (CommonUtils.isErlangFileContentFileName(file.getName())) {
                        final IErlModule m = (IErlModule) model.create(file);
                        children.add(m);
                    }
                }
            }

            setChildren(children);

        } catch (final CoreException e) {
            ErlLogger.error(e);
            setChildren(new ArrayList<IErlModule>());
            return false;
        }
        return true;
    }

    private void addOtpExternals(final List<IErlElement> children) {
        final String name = "OTP "
                + getProperties().getRuntimeVersion().toString();
        final IErlExternalRoot external = new ErlOtpExternalReferenceEntryList(
                this, name);
        children.add(external);
    }

    private void addExternals(final List<IErlElement> children) {
        final String externalIncludes = getExternalIncludesString();
        final String externalModules = getExternalModulesString();
        final Collection<IPath> includeDirs = getIncludeDirs();
        final List<String> projectIncludes = Lists.newArrayList();
        for (final IPath path : includeDirs) {
            if (path.isAbsolute() && !fProject.getLocation().isPrefixOf(path)) {
                final Collection<String> includes = ErlangEngine.getInstance()
                        .getService(OpenService.class)
                        .getIncludesInDir(path.toPortableString());
                if (includes != null) {
                    for (final String include : includes) {
                        projectIncludes.add(path.append(include)
                                .toPortableString());
                    }
                }
            }
        }
        if (externalIncludes.length() != 0 || externalModules.length() != 0
                || !projectIncludes.isEmpty()) {
            final IErlExternalRoot external = new ErlExternalReferenceEntryList(
                    this, "Externals", externalIncludes, projectIncludes,
                    externalModules);
            children.add(external);
        }
    }

    /**
     * Removes the Erlang nature from the project.
     */
    public void deconfigure() throws CoreException {
        // unregister Erlang builder
        removeFromBuildSpec(ModelPlugin.BUILDER_ID);
    }

    /**
     * Returns a default output location. This is the project bin folder
     */
    protected IPath defaultOutputLocation() {
        return fProject.getFullPath().append("ebin"); //$NON-NLS-1$
    }

    /**
     * Returns true if this handle represents the same Erlang project as the
     * given handle. Two handles represent the same project if they are
     * identical or if they represent a project with the same underlying
     * resource and occurrence counts.
     * 
     * @see ErlElement#equals(Object)
     */
    @Override
    public boolean equals(final Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof ErlProject)) {
            return false;
        }

        final ErlProject other = (ErlProject) o;
        return fProject.equals(other.getWorkspaceProject());
    }

    @Override
    public boolean exists() {
        return NatureUtil.hasErlangNature(fProject);
    }

    /**
     * Remove all markers denoting classpath problems
     */
    protected void flushCodepathProblemMarkers(final boolean flushCycleMarkers,
            final boolean flushCodepathFormatMarkers) {
        try {
            if (fProject.isAccessible()) {
                final IMarker[] markers = fProject.findMarkers(
                        IErlModelMarker.BUILDPATH_PROBLEM_MARKER, false,
                        IResource.DEPTH_ZERO);
                for (final IMarker marker : markers) {
                    if (flushCycleMarkers && flushCodepathFormatMarkers) {
                        marker.delete();
                    }
                }
            }
        } catch (final CoreException e) {
            // could not flush markers: not much we can do
            if (ModelConfig.verbose) {
                ErlLogger.warn(e);
            }
        }
    }

    /**
     * @see IErlElement
     */
    @Override
    public ErlElementKind getKind() {
        return ErlElementKind.PROJECT;
    }

    /**
     * Returns an array of non-Erlang resources contained in the receiver.
     */
    public Collection<IResource> getNonErlangResources() {
        return getNonErlangResources(this);
    }

    /**
     * @see IErlElement
     */
    @Override
    public IResource getResource() {
        return getCorrespondingResource();
    }

    @Override
    public IResource getCorrespondingResource() {
        return fProject;
    }

    @Override
    public int hashCode() {
        if (fProject == null) {
            return super.hashCode();
        }
        return fProject.hashCode();
    }

    /**
     * Removes the given builder from the build spec for the given project.
     */
    protected void removeFromBuildSpec(final String builderID)
            throws CoreException {

        final IProjectDescription description = fProject.getDescription();
        final ICommand[] commands = description.getBuildSpec();
        for (int i = 0; i < commands.length; ++i) {
            if (commands[i].getBuilderName().equals(builderID)) {
                final ICommand[] newCommands = new ICommand[commands.length - 1];
                System.arraycopy(commands, 0, newCommands, 0, i);
                System.arraycopy(commands, i + 1, newCommands, i,
                        commands.length - i - 1);
                description.setBuildSpec(newCommands);
                fProject.setDescription(description, null);
                return;
            }
        }
    }

    /**
     * Answers an PLUGIN_ID which is used to distinguish project/entries during
     * package fragment root computations
     * 
     * @return String
     */
    public String rootID() {
        return "[PRJ]" + fProject.getFullPath(); //$NON-NLS-1$
    }

    @Override
    public Collection<IErlModule> getModules() throws ErlModelException {
        final List<IErlModule> modulesForProject = ErlModelCache.getDefault()
                .getModulesForProject(this);
        if (modulesForProject != null) {
            return modulesForProject;
        }
        final List<IErlModule> result = new ArrayList<IErlModule>();
        final List<IPath> sourceDirs = Lists.newArrayList(getSourceDirs());
        for (final IPath s : SourcePathUtils
                .getExtraSourcePathsForModel(fProject)) {
            sourceDirs.add(s);
        }
        result.addAll(getModulesOrIncludes(fProject, ErlangEngine.getInstance()
                .getModel(), sourceDirs, true));
        ErlModelCache.getDefault().putModulesForProject(this, result);
        return result;
    }

    private static List<IErlModule> getModulesOrIncludes(
            final IProject project, final IErlElementLocator model,
            final Collection<IPath> dirs, final boolean getModules)
            throws ErlModelException {
        final List<IErlModule> result = Lists.newArrayList();
        for (final IPath dir : dirs) {
            final IFolder folder = project.getFolder(dir);
            final IErlElement element = model.findElement(folder, true);
            if (element instanceof IErlFolder) {
                final IErlFolder erlFolder = (IErlFolder) element;
                erlFolder.open(null);
                for (final IErlElement e : erlFolder
                        .getChildrenOfKind(ErlElementKind.MODULE)) {
                    if (e instanceof IErlModule) {
                        final IErlModule m = (IErlModule) e;
                        final boolean isModule = ModuleKind.nameToModuleKind(m
                                .getName()) != ModuleKind.HRL;
                        if (isModule == getModules) {
                            result.add(m);
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public Collection<IErlModule> getModulesAndIncludes()
            throws ErlModelException {
        final List<IErlModule> result = new ArrayList<IErlModule>();
        final ErlModelCache erlModelCache = ErlModelCache.getDefault();
        final List<IErlModule> modulesForProject = erlModelCache
                .getModulesForProject(this);
        final List<IErlModule> includesForProject = erlModelCache
                .getIncludesForProject(this);
        if (modulesForProject != null && includesForProject != null) {
            result.addAll(modulesForProject);
            result.addAll(includesForProject);
        } else {
            final List<IErlModule> cached = erlModelCache
                    .getModulesForProject(this);
            final IErlElementLocator model = ErlangEngine.getInstance()
                    .getModel();
            if (cached == null) {
                final List<IErlModule> modules = getModulesOrIncludes(fProject,
                        model, getSourceDirs(), true);
                result.addAll(modules);
            } else {
                result.addAll(cached);
            }
            final Collection<IErlModule> includes = getIncludes();
            result.addAll(includes);
        }
        return result;
    }

    @Override
    public Collection<IErlModule> getIncludes() throws ErlModelException {
        final ErlModelCache erlModelCache = ErlModelCache.getDefault();
        final List<IErlModule> cached = erlModelCache
                .getIncludesForProject(this);
        if (cached != null) {
            return cached;
        }
        final List<IErlModule> includes = getModulesOrIncludes(fProject,
                ErlangEngine.getInstance().getModel(), getIncludeDirs(), false);
        erlModelCache.putIncludesForProject(this, includes);
        return includes;
    }

    /**
     * Returns a canonicalized path from the given external path. Note that the
     * return path contains the same number of segments and it contains a device
     * only if the given path contained one.
     * 
     * @param externalPath
     *            IPath
     * @see java.io.File for the definition of a canonicalized path
     * @return IPath
     */
    public static IPath canonicalizedPath(final IPath externalPath) {

        if (externalPath == null) {
            return null;
        }

        if (IS_CASE_SENSITIVE) {
            return externalPath;
        }

        // if not external path, return original path
        final IWorkspace workspace = ResourcesPlugin.getWorkspace();
        if (workspace == null) {
            return externalPath; // protection during shutdown (30487)
        }
        if (workspace.getRoot().findMember(externalPath) != null) {
            return externalPath;
        }

        IPath canonicalPath = null;
        try {
            canonicalPath = new Path(
                    new File(externalPath.toOSString()).getCanonicalPath());
        } catch (final IOException e) {
            // default to original path
            return externalPath;
        }

        IPath result;
        final int canonicalLength = canonicalPath.segmentCount();
        if (canonicalLength == 0) {
            // the java.io.File canonicalization failed
            return externalPath;
        } else if (externalPath.isAbsolute()) {
            result = canonicalPath;
        } else {
            // if path is relative, remove the first segments that were added by
            // the java.io.File canonicalization
            // e.g. 'lib/classes.zip' was converted to
            // 'd:/myfolder/lib/classes.zip'
            final int externalLength = externalPath.segmentCount();
            if (canonicalLength >= externalLength) {
                result = canonicalPath.removeFirstSegments(canonicalLength
                        - externalLength);
            } else {
                return externalPath;
            }
        }

        // keep device only if it was specified (this is because
        // File.getCanonicalPath() converts '/lib/classed.zip' to
        // 'd:/lib/classes/zip')
        if (externalPath.getDevice() == null) {
            result = result.setDevice(null);
        }
        return result;
    }

    /**
     * Returns an array of non-Erlang resources contained in the receiver.
     */
    private Collection<IResource> getNonErlangResources(
            final IErlProject project) {

        if (nonErlangResources == null) {
            nonErlangResources = Lists.newArrayList();
        }
        return Collections.unmodifiableCollection(nonErlangResources);
    }

    @Override
    public IErlModule getModule(final String name) {
        try {
            return ErlangEngine
                    .getInstance()
                    .getModel()
                    .findModuleFromProject(this, name, null, false,
                            IErlElementLocator.Scope.PROJECT_ONLY);
        } catch (final ErlModelException e) {
            // final boolean hasExtension = ListsUtils.hasExtension(name);
            return null;
        }
    }

    @Override
    public ErlangProjectProperties getProperties() {
        if (properties == null) {

            configurationChanged();
        }
        return properties;
    }

    public IEclipsePreferences getCorePropertiesNode() {
        return new ProjectScope(fProject).getNode("org.erlide.model");
    }

    @Override
    public Collection<IErlModule> getExternalModules() throws ErlModelException {
        final List<IErlModule> result = Lists.newArrayList();
        accept(new IErlElementVisitor() {

            @Override
            public boolean visit(final IErlElement element)
                    throws ErlModelException {
                final boolean isExternalOrProject = element.getKind() == ErlElementKind.EXTERNAL
                        || element.getKind() == ErlElementKind.PROJECT;
                if (element instanceof IErlModule) {
                    final IErlModule module = (IErlModule) element;
                    result.add(module);
                    return false;
                } else if (isExternalOrProject && element instanceof IOpenable) {
                    final IOpenable openable = (IOpenable) element;
                    openable.open(null);
                }
                return isExternalOrProject;
            }
        }, EnumSet.noneOf(AcceptFlags.class), ErlElementKind.MODULE);
        return result;
    }

    @Override
    public void resourceChanged(final IResourceDelta delta) {
        if (delta == null) {
            return;
        }
        if ((delta.getFlags() & IResourceDelta.DESCRIPTION) != 0) {
            // TODO when we have cache in ErlModuleMap for referenced projects,
            // we should purge it here
        }
        if ((delta.getFlags() & ~IResourceDelta.MARKERS) != 0) {
            super.resourceChanged(delta);
            // FIXME when should we call getModelCache().removeProject(this); ?
        }
    }

    private String getExternal(final ExternalKind external) {
        final IPreferencesService service = Platform.getPreferencesService();
        final String key = external == ExternalKind.EXTERNAL_INCLUDES ? "default_external_includes"
                : "default_external_modules";
        String result = getExternal(external, service, key, "org.erlide.ui");
        if (Strings.isNullOrEmpty(result)) {
            result = getExternal(external, service, key, "org.erlide.core");
        }
        return result;
    }

    private String getExternal(final ExternalKind external,
            final IPreferencesService service, final String key,
            final String pluginId) {
        final String global = service.getString(pluginId, key, "", null);
        final ErlangProjectProperties prefs = getProperties();
        final String projprefs = external == ExternalKind.EXTERNAL_INCLUDES ? prefs
                .getExternalIncludesFile() : prefs.getExternalModulesFile();
        return PreferencesUtils.packArray(new String[] { projprefs, global });
    }

    @Override
    public String getExternalModulesString() {
        final String externalModulesString = getExternal(ExternalKind.EXTERNAL_MODULES);
        return externalModulesString;
    }

    @Override
    public String getExternalIncludesString() {
        final String externalIncludesString = getExternal(ExternalKind.EXTERNAL_INCLUDES);
        return externalIncludesString;
    }

    public void setIncludeDirs(final Collection<IPath> includeDirs) {
        getModelCache().removeProject(this);
        getProperties().setIncludeDirs(includeDirs);
        storeProperties();
        setStructureKnown(false);
    }

    public void setSourceDirs(final Collection<IPath> sourceDirs) {
        getModelCache().removeProject(this);
        getProperties().setSourceDirs(sourceDirs);
        storeProperties();
        setStructureKnown(false);
    }

    public void setExternalModulesFile(final String absolutePath) {
        getModelCache().removeProject(this);
        getProperties().setExternalModulesFile(absolutePath);
        storeProperties();
        setStructureKnown(false);
    }

    public void setExternalIncludesFile(final String absolutePath) {
        getModelCache().removeProject(this);
        getProperties().setExternalIncludesFile(absolutePath);
        storeProperties();
        setStructureKnown(false);
    }

    @Override
    public Collection<IPath> getSourceDirs() {
        Collection<IPath> sourceDirs = getProperties().getSourceDirs();
        sourceDirs = resolvePaths(sourceDirs);
        return sourceDirs;
    }

    @Override
    public Collection<IPath> getIncludeDirs() {
        Collection<IPath> includeDirs = getProperties().getIncludeDirs();
        includeDirs = resolvePaths(includeDirs);
        return includeDirs;
    }

    private Collection<IPath> resolvePaths(final Collection<IPath> paths) {
        final IPathVariableManager pathVariableManager = ResourcesPlugin
                .getWorkspace().getPathVariableManager();
        final List<IPath> result = Lists.newArrayListWithCapacity(paths.size());
        for (final IPath includeDir : paths) {
            @SuppressWarnings("deprecation")
            final IPath resolvedPath = pathVariableManager
                    .resolvePath(includeDir);
            result.add(resolvedPath);
        }
        return Collections.unmodifiableCollection(result);
    }

    @Override
    public IPath getOutputLocation() {
        return getProperties().getOutputDir();
    }

    @Override
    public RuntimeInfo getRuntimeInfo() {
        return getProperties().getRuntimeInfo();
    }

    @Override
    public RuntimeVersion getRuntimeVersion() {
        return getProperties().getRuntimeVersion();
    }

    private final static IPath DOT_PATH = new Path(".");

    @Override
    public boolean hasSourceDir(final IPath path) {
        if (path.equals(DOT_PATH)) {
            return true;
        }
        final IPath f = path.removeFirstSegments(1);
        for (final IPath s : getSourceDirs()) {
            if (s.equals(f)) {
                return true;
            }
            // if (fullPath.segmentCount() == 1 && s.toString().equals(".")) {
            // return true;
            // }
        }
        return false;
    }

    @Override
    public void setProperties(final ErlangProjectProperties properties)
            throws BackingStoreException {
        getModelCache().removeProject(this);
        final ErlangProjectProperties projectProperties = getProperties();
        projectProperties.copyFrom(properties);
        storeProperties();
    }

    @Override
    public void clearCaches() {
        getModelCache().removeProject(this);
    }

    @Override
    public Collection<IErlProject> getReferencedProjects()
            throws ErlModelException {
        final List<IErlProject> result = Lists.newArrayList();
        try {
            for (final IProject project : fProject.getReferencedProjects()) {
                final IErlProject p = ErlangEngine.getInstance().getModel()
                        .getErlangProject(project);
                if (p != null) {
                    result.add(p);
                }
            }
        } catch (final CoreException e) {
            // throw new ErlModelException(e);
        }
        return result;
    }

    @Override
    public Collection<IErlModule> getExternalIncludes()
            throws ErlModelException {
        final List<IErlModule> result = Lists.newArrayList();
        accept(new IErlElementVisitor() {

            @Override
            public boolean visit(final IErlElement element)
                    throws ErlModelException {
                final boolean isExternalOrProject = element.getKind() == ErlElementKind.EXTERNAL
                        || element.getKind() == ErlElementKind.PROJECT;
                if (element instanceof IErlModule) {
                    final IErlModule module = (IErlModule) element;
                    result.add(module);
                    return false;
                } else if (isExternalOrProject) {
                    if (element instanceof IErlExternal) {
                        final IErlExternal external = (IErlExternal) element;
                        if (!external.hasIncludes()) {
                            return false;
                        }
                    }
                    if (element instanceof IOpenable) {
                        final IOpenable openable = (IOpenable) element;
                        openable.open(null);
                    }
                    return true;
                }
                return false;
            }
        }, EnumSet.noneOf(AcceptFlags.class), ErlElementKind.MODULE);
        return result;
    }

    public void pathVarsChanged() {
        clearCaches();
    }

    public boolean moduleInProject(final IErlModule module) {
        final IErlProject project = ErlangEngine.getInstance()
                .getModelUtilService().getProject(module);
        if (project == null) {
            return false;
        }
        return equals(project);
    }

    @Override
    public void dispose() {
        clearCaches();
        try {
            accept(new IErlElementVisitor() {

                @Override
                public boolean visit(final IErlElement element)
                        throws ErlModelException {
                    element.dispose();
                    return false;
                }
            }, EnumSet.of(AcceptFlags.CHILDREN_FIRST, AcceptFlags.LEAFS_ONLY),
                    ErlElementKind.MODULE);
        } catch (final ErlModelException e) {
        }
        super.dispose();
    }

    @Override
    public IProject getWorkspaceProject() {
        return fProject;
    }

    @Override
    public void close() throws ErlModelException {
        clearCaches();
        super.close();
    }

    public void loadCoreProperties() {
        final IEclipsePreferences node = getCorePropertiesNode();
        builderTool = BuilderTool.valueOf(node.get("builderTool",
                BuilderTool.INTERNAL.name()));
        builderConfig = BuilderConfig.valueOf(node.get("builderConfig",
                BuilderConfig.INTERNAL.name()));
    }

    public void saveCoreProperties() {
        final IEclipsePreferences node = getCorePropertiesNode();
        node.put("builderTool", builderTool.name());
        node.put("builderConfig", builderConfig.name());
        try {
            node.flush();
        } catch (final BackingStoreException e) {
            // ignore?
        }
    }

    public ErlangProjectProperties loadProperties() {
        final ProjectConfigurationPersister persister = ErlangBuilder
                .getFactory().getConfigurationPersister(builderConfig);
        persister.setProject(getWorkspaceProject());
        return persister.getConfiguration(this);
    }

    private void storeProperties() {
        final ProjectConfigurationPersister persister = ErlangBuilder
                .getFactory().getConfigurationPersister(builderConfig);
        persister.setProject(getWorkspaceProject());
        if (properties != null) {
            persister.setConfiguration(this, properties);
        }
    }

    @Override
    public BuilderTool getBuilderTool() {
        return builderTool;
    }

    @Override
    public void setBuilderTool(final BuilderTool tool) {
        if (builderTool == tool) {
            return;
        }
        builderTool = tool;
        final Collection<BuilderConfig> configs = builderTool
                .getMatchingConfigs();
        if (configs.size() == 1) {
            setBuilderConfig(configs.iterator().next());
        } else if (!configs.contains(builderConfig)) {
            setBuilderConfig(null);
        }
    }

    @Override
    public BuilderConfig getBuilderConfig() {
        return builderConfig;
    }

    @Override
    public void setBuilderConfig(final BuilderConfig config) {
        if (config != null
                && !builderTool.getMatchingConfigs().contains(config)) {
            throw new IllegalArgumentException(String.format(
                    "Builder config %s can't be used with tool %s", config,
                    builderTool));
        }

        // TODO unsubscribe from notifications from old config

        builderConfig = config;

        // TODO subscribe to notifications from new config
    }

    public boolean hasConfigurationFor(final BuilderConfig config) {
        if (!exists()) {
            return false;
        }
        return getConfig(config) != null;
    }

    /**
     * Returns the detected configuration for the project. Returns null if
     * impossible (project doesn't exist or files not available).
     */
    private ErlangProjectProperties getConfig(final BuilderConfig config) {
        final ProjectConfigurationPersister persister = ErlangBuilder
                .getFactory().getConfigurationPersister(config);
        if (persister == null) {
            return null;
        }
        return persister.getConfiguration(this);
    }

    public ErlangProjectProperties getConfig() {
        return getConfig(builderConfig);
    }

    @Override
    public void configurationChanged() {
        loadCoreProperties();
        properties = loadProperties();
    }

}

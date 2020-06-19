package org.erlide.ui.properties;

import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

public class StringToIPathConverter implements IConverter<String, IPath> {
    @Override
    public Object getToType() {
        return IPath.class;
    }

    @Override
    public Object getFromType() {
        return String.class;
    }

    @Override
    public IPath convert(final String fromObject) {
        return new Path(fromObject);
    }
}

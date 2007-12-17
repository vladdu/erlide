/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.erlide.ui.views.outline;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.erlide.ui.ErlideUIPlugin;
import org.erlide.ui.ErlideUIPluginImages;

/**
 * A JavaImageDescriptor consists of a base image and several adornments. The
 * adornments are computed according to the flags either passed during creation
 * or set via the method <code>setAdornments</code>.
 * 
 * <p>
 * This class may be instantiated; it is not intended to be subclassed.
 * </p>
 * 
 * @since 2.0
 */
public class ErlangElementImageDescriptor extends CompositeImageDescriptor {

	/** Flag to render the 'exported' adornment. */
	public static final int EXPORTED = 0x001;

	public static final int ERROR = 0x100;

	public static final int WARNING = 0x200;

	private final ImageDescriptor fBaseImage;

	private int fFlags;

	private Point fSize;

	/**
	 * Creates a new JavaElementImageDescriptor.
	 * 
	 * @param baseImage
	 *            an image descriptor used as the base image
	 * @param flags
	 *            flags indicating which adornments are to be rendered. See
	 *            <code>setAdornments</code> for valid values.
	 * @param size
	 *            the size of the resulting image
	 * @see #setAdornments(int)
	 */
	public ErlangElementImageDescriptor(ImageDescriptor baseImage, int flags,
			Point size) {
		fBaseImage = baseImage;
		Assert.isNotNull(fBaseImage);
		fFlags = flags;
		Assert.isTrue(fFlags >= 0);
		fSize = size;
		Assert.isNotNull(fSize);
	}

	/**
	 * Sets the descriptors adornments. Valid values are: <code>EXPORTED</code>,
	 * or any combination of those.
	 * 
	 * @param adornments
	 *            the image descriptors adornments
	 */
	public void setAdornments(int adornments) {
		Assert.isTrue(adornments >= 0);
		fFlags = adornments;
	}

	/**
	 * Returns the current adornments.
	 * 
	 * @return the current adornments
	 */
	public int getAdronments() {
		return fFlags;
	}

	/**
	 * Sets the size of the image created by calling <code>createImage()</code>.
	 * 
	 * @param size
	 *            the size of the image returned from calling
	 *            <code>createImage()</code>
	 * @see ImageDescriptor#createImage()
	 */
	public void setImageSize(Point size) {
		Assert.isNotNull(size);
		Assert.isTrue(size.x >= 0 && size.y >= 0);
		fSize = size;
	}

	/**
	 * Returns the size of the image created by calling
	 * <code>createImage()</code>.
	 * 
	 * @return the size of the image created by calling
	 *         <code>createImage()</code>
	 * @see ImageDescriptor#createImage()
	 */
	public Point getImageSize() {
		return new Point(fSize.x, fSize.y);
	}

	/*
	 * (non-Javadoc) Method declared in CompositeImageDescriptor
	 */
	@Override
	protected Point getSize() {
		return fSize;
	}

	/*
	 * (non-Javadoc) Method declared on Object.
	 */
	@Override
	public boolean equals(Object object) {
		if (object == null
				|| !ErlangElementImageDescriptor.class
						.equals(object.getClass())) {
			return false;
		}

		final ErlangElementImageDescriptor other = (ErlangElementImageDescriptor) object;
		return (fBaseImage.equals(other.fBaseImage) && fFlags == other.fFlags && fSize
				.equals(other.fSize));
	}

	/*
	 * (non-Javadoc) Method declared on Object.
	 */
	@Override
	public int hashCode() {
		return fBaseImage.hashCode() | fFlags | fSize.hashCode();
	}

	/*
	 * (non-Javadoc) Method declared in CompositeImageDescriptor
	 */
	@Override
	protected void drawCompositeImage(int width, int height) {
		final ImageData bg = getImageData(fBaseImage);

		drawImage(bg, 0, 0);

		drawTopRight();
		drawBottomRight();
		drawBottomLeft();
	}

	private ImageData getImageData(ImageDescriptor descriptor) {
		ImageData data = descriptor.getImageData();
		if (data == null) {
			data = DEFAULT_IMAGE_DATA;
			ErlideUIPlugin
					.logErrorMessage("Image data not available: " + descriptor.toString()); //$NON-NLS-1$
		}
		return data;
	}

	private void drawTopRight() {
	}

	private void drawBottomRight() {
	}

	private void drawBottomLeft() {
		final Point size = getSize();
		int x = 0;
		if ((fFlags & ERROR) != 0) {
			final ImageData data = getImageData(ErlideUIPluginImages.DESC_OVR_ERROR);
			drawImage(data, x, size.y - data.height);
			x += data.width;
		}
		if ((fFlags & WARNING) != 0) {
			final ImageData data = getImageData(ErlideUIPluginImages.DESC_OVR_WARNING);
			drawImage(data, x, size.y - data.height);
			x += data.width;
		}

	}
}

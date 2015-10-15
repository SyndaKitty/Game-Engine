/*
 * Copyright LWJGL. All rights reserved.
 * License terms: http://lwjgl.org/license.php
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
package org.lwjgl.openal;

/**
 * Native bindings to the <a href="http://kcat.strangesoft.net/openal-extensions/SOFT_loop_points.txt">SOFT_loop_points</a> extension.
 * 
 * <p>This extension allows an application to specify the offsets at which a buffer loops. Unextended OpenAL only allows for a source to loop a whole buffer,
 * or all buffers of a queue, which is not desirable for sounds that may want a lead-in or lead-out along with the looping portion.</p>
 */
public final class SOFTLoopPoints {

	/** Accepted by the {@code paramName} parameter of alBufferiv and alGetBufferiv. */
	public static final int AL_LOOP_POINTS_SOFT = 0x2015;

	private SOFTLoopPoints() {}

}
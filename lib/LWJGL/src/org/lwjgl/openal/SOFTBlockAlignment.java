/*
 * Copyright LWJGL. All rights reserved.
 * License terms: http://lwjgl.org/license.php
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
package org.lwjgl.openal;

/**
 * Native bindings to the <a href="http://kcat.strangesoft.net/openal-extensions/SOFT_block_alignment.txt">SOFT_block_alignment</a> extension.
 * 
 * <p>This extension provides a mechanism for specifying block alignment properties for sample data. This is useful for, though not strictly limited to, ADPCM
 * compression where the block alignment is specified in the media file header instead of the data stream, and controls the decoding process.</p>
 */
public final class SOFTBlockAlignment {

	/** Accepted by the {@code paramName} parameter of alBufferi, alBufferiv, alGetBufferi, and alGetBufferiv. */
	public static final int
		AL_UNPACK_BLOCK_ALIGNMENT_SOFT = 0x200C,
		AL_PACK_BLOCK_ALIGNMENT_SOFT   = 0x200D;

	private SOFTBlockAlignment() {}

}
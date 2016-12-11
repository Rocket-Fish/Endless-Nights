package com.bearfishapps.shadowarcher.Physics;

public class CollisionMasks {
    public static short
            Mask_DEFAULT = 0x01,
            Mask_ARM = 0x02,
            Mask_LEG = 0x04,
            Mask_BODY = 0x08,
            Mask_HEAD = 0x16,
            Mask_BOW = 0x32
                    ;
    // note that they numbers much be x2 each time or binary doesnt work out !!!
    // 01 and 10 mask to make 11,
}

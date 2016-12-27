package com.bearfishapps.shadowarcher.Physics.Collision;

import com.badlogic.gdx.Gdx;

public class CollisionMasks {
    public static short
            Mask_DEFAULT    = 0x001,
            Mask_ARM        = 0x002,
            Mask_LEG        = 0x004,
            Mask_BODY       = 0x008,
            Mask_HEAD       = 0x010,
            Mask_Humanoid   = (short)(Mask_ARM | Mask_LEG | Mask_BODY | Mask_HEAD),
            Mask_BOW        = 0x020,
            Mask_ARROW      = 0x040,
            Mask_PELLETS    = 0x080,
            Mask_EVERYTHING = 0xFFF
                    ;
    // 01 and 10 mask to make 11,
    public static void printShorts() {
        Gdx.app.log("Mask_DEFAULT :", Integer.toBinaryString(0xFFFF & Mask_DEFAULT ));
        Gdx.app.log("Mask_ARM     :", Integer.toBinaryString(0xFFFF & Mask_ARM     ));
        Gdx.app.log("Mask_LEG     :", Integer.toBinaryString(0xFFFF & Mask_LEG     ));
        Gdx.app.log("Mask_BODY    :", Integer.toBinaryString(0xFFFF & Mask_BODY    ));
        Gdx.app.log("Mask_HEAD    :", Integer.toBinaryString(0xFFFF & Mask_HEAD    ));
        Gdx.app.log("Mask_Humanoid:", Integer.toBinaryString(0xFFFF & Mask_Humanoid));
        Gdx.app.log("Mask_BOW     :", Integer.toBinaryString(0xFFFF & Mask_BOW     ));
        Gdx.app.log("Mask_ARROW   :", Integer.toBinaryString(0xFFFF & Mask_ARROW   ));
        Gdx.app.log("Mask_All     :", Integer.toBinaryString(0xFFFF & Mask_EVERYTHING));
    }
}

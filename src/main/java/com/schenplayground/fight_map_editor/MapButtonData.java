package com.schenplayground.fight_map_editor;

public class MapButtonData {
    private int assetIndex;
    private AssetType type;
    private String data;

    public MapButtonData(int assetIndex, AssetType type, String data) {
        this.assetIndex = assetIndex;
        this.type = type;
        this.data = data;
    }

    public int getAssetIndex() {
        return assetIndex;
    }

    public void setAssetIndex(int assetIndex) {
        this.assetIndex = assetIndex;
    }

    public AssetType getType() {
        return type;
    }

    public void setType(AssetType type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

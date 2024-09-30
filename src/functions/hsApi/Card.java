package functions.hsApi;

import java.util.ArrayList;

public class Card{
    public int id;
    public int collectible;
    public String slug;
    public int classId;
    public ArrayList<Integer> multiClassIds;
    public int spellSchoolId;
    public int cardTypeId;
    public int cardSetId;
    public int rarityId;
    public String artistName;
    public int manaCost;
    public String name;
    public String text;
    public String image;
    public String imageGold;
    public String flavorText;
    public String cropImage;
    public ArrayList<Integer> keywordIds;
    public boolean isZilliaxFunctionalModule;
    public boolean isZilliaxCosmeticModule;
    public Duels duels;
    public int copyOfCardId;
    public int health;
    public int attack;
    public int minionTypeId;
    public ArrayList<Integer> childIds;
    public RuneCost runeCost;

    
    
    public Card() {
    }

    public Card(int id, int collectible, String slug, int classId, ArrayList<Integer> multiClassIds, int spellSchoolId,
            int cardTypeId, int cardSetId, int rarityId, String artistName, int manaCost, String name, String text,
            String image, String imageGold, String flavorText, String cropImage, ArrayList<Integer> keywordIds,
            boolean isZilliaxFunctionalModule, boolean isZilliaxCosmeticModule, Duels duels, int copyOfCardId,
            int health, int attack, int minionTypeId, ArrayList<Integer> childIds, RuneCost runeCost) {
        this.id = id;
        this.collectible = collectible;
        this.slug = slug;
        this.classId = classId;
        this.multiClassIds = multiClassIds;
        this.spellSchoolId = spellSchoolId;
        this.cardTypeId = cardTypeId;
        this.cardSetId = cardSetId;
        this.rarityId = rarityId;
        this.artistName = artistName;
        this.manaCost = manaCost;
        this.name = name;
        this.text = text;
        this.image = image;
        this.imageGold = imageGold;
        this.flavorText = flavorText;
        this.cropImage = cropImage;
        this.keywordIds = keywordIds;
        this.isZilliaxFunctionalModule = isZilliaxFunctionalModule;
        this.isZilliaxCosmeticModule = isZilliaxCosmeticModule;
        this.duels = duels;
        this.copyOfCardId = copyOfCardId;
        this.health = health;
        this.attack = attack;
        this.minionTypeId = minionTypeId;
        this.childIds = childIds;
        this.runeCost = runeCost;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getCollectible() {
        return collectible;
    }
    public void setCollectible(int collectible) {
        this.collectible = collectible;
    }
    public String getSlug() {
        return slug;
    }
    public void setSlug(String slug) {
        this.slug = slug;
    }
    public int getClassId() {
        return classId;
    }
    public void setClassId(int classId) {
        this.classId = classId;
    }
    public ArrayList<Integer> getMultiClassIds() {
        return multiClassIds;
    }
    public void setMultiClassIds(ArrayList<Integer> multiClassIds) {
        this.multiClassIds = multiClassIds;
    }
    public int getSpellSchoolId() {
        return spellSchoolId;
    }
    public void setSpellSchoolId(int spellSchoolId) {
        this.spellSchoolId = spellSchoolId;
    }
    public int getCardTypeId() {
        return cardTypeId;
    }
    public void setCardTypeId(int cardTypeId) {
        this.cardTypeId = cardTypeId;
    }
    public int getCardSetId() {
        return cardSetId;
    }
    public void setCardSetId(int cardSetId) {
        this.cardSetId = cardSetId;
    }
    public int getRarityId() {
        return rarityId;
    }
    public void setRarityId(int rarityId) {
        this.rarityId = rarityId;
    }
    public String getArtistName() {
        return artistName;
    }
    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
    public int getManaCost() {
        return manaCost;
    }
    public void setManaCost(int manaCost) {
        this.manaCost = manaCost;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getImageGold() {
        return imageGold;
    }
    public void setImageGold(String imageGold) {
        this.imageGold = imageGold;
    }
    public String getFlavorText() {
        return flavorText;
    }
    public void setFlavorText(String flavorText) {
        this.flavorText = flavorText;
    }
    public String getCropImage() {
        return cropImage;
    }
    public void setCropImage(String cropImage) {
        this.cropImage = cropImage;
    }
    public ArrayList<Integer> getKeywordIds() {
        return keywordIds;
    }
    public void setKeywordIds(ArrayList<Integer> keywordIds) {
        this.keywordIds = keywordIds;
    }
    public boolean isZilliaxFunctionalModule() {
        return isZilliaxFunctionalModule;
    }
    public void setZilliaxFunctionalModule(boolean isZilliaxFunctionalModule) {
        this.isZilliaxFunctionalModule = isZilliaxFunctionalModule;
    }
    public boolean isZilliaxCosmeticModule() {
        return isZilliaxCosmeticModule;
    }
    public void setZilliaxCosmeticModule(boolean isZilliaxCosmeticModule) {
        this.isZilliaxCosmeticModule = isZilliaxCosmeticModule;
    }
    public Duels getDuels() {
        return duels;
    }
    public void setDuels(Duels duels) {
        this.duels = duels;
    }
    public int getCopyOfCardId() {
        return copyOfCardId;
    }
    public void setCopyOfCardId(int copyOfCardId) {
        this.copyOfCardId = copyOfCardId;
    }
    public int getHealth() {
        return health;
    }
    public void setHealth(int health) {
        this.health = health;
    }
    public int getAttack() {
        return attack;
    }
    public void setAttack(int attack) {
        this.attack = attack;
    }
    public int getMinionTypeId() {
        return minionTypeId;
    }
    public void setMinionTypeId(int minionTypeId) {
        this.minionTypeId = minionTypeId;
    }
    public ArrayList<Integer> getChildIds() {
        return childIds;
    }
    public void setChildIds(ArrayList<Integer> childIds) {
        this.childIds = childIds;
    }
    public RuneCost getRuneCost() {
        return runeCost;
    }
    public void setRuneCost(RuneCost runeCost) {
        this.runeCost = runeCost;
    }
    @Override
    public String toString() {
        return "Card [id=" + id + ", collectible=" + collectible + ", slug=" + slug + ", classId=" + classId
                + ", multiClassIds=" + multiClassIds + ", spellSchoolId=" + spellSchoolId + ", cardTypeId=" + cardTypeId
                + ", cardSetId=" + cardSetId + ", rarityId=" + rarityId + ", artistName=" + artistName + ", manaCost="
                + manaCost + ", name=" + name + ", text=" + text + ", image=" + image + ", imageGold=" + imageGold
                + ", flavorText=" + flavorText + ", cropImage=" + cropImage + ", keywordIds=" + keywordIds
                + ", isZilliaxFunctionalModule=" + isZilliaxFunctionalModule + ", isZilliaxCosmeticModule="
                + isZilliaxCosmeticModule + ", duels=" + duels + ", copyOfCardId=" + copyOfCardId + ", health=" + health
                + ", attack=" + attack + ", minionTypeId=" + minionTypeId + ", childIds=" + childIds + ", runeCost="
                + runeCost + "]";
    }

    
}
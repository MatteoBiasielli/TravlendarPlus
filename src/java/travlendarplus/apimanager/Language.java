package travlendarplus.apimanager;

public enum Language {
    IT("it"), EN("en"), FR("fr");
    private String lang;
    Language(String s){
        this.lang=s;
    }
    public String getLang(){
        return this.lang;
    }
	
}

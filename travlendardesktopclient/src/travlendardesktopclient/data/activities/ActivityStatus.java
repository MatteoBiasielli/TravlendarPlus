package travlendardesktopclient.data.activities;


public enum ActivityStatus {
	NOT_STARTED(1),TERMINATED(3), ON_GOING(2);
	private int i;
	ActivityStatus(int i){
		this.i=i;
	}
        /**
         * 
         * @return the value associated to the object 
         */
	public int getValue(){
		return i;
	}

}

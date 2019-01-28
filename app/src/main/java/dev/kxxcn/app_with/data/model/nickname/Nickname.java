package dev.kxxcn.app_with.data.model.nickname;

/**
 * Created by kxxcn on 2018-11-14.
 */
public class Nickname {

	private String uniqueIdentifier;
	private String myNickname;
	private String yourNickname;
	private boolean isFemale;

	public String getUniqueIdentifier() {
		return uniqueIdentifier;
	}

	public void setUniqueIdentifier(String uniqueIdentifier) {
		this.uniqueIdentifier = uniqueIdentifier;
	}

	public void setMyNickname(String myNickname) {
		this.myNickname = myNickname;
	}

	public void setYourNickname(String yourNickname) {
		this.yourNickname = yourNickname;
	}

	public void setFemale(boolean female) {
		isFemale = female;
	}

}


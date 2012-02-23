package scu.im.bean;

public class Policy {

	private String id;
	private String uid;
	private int friendPolicyId;
	private int safePolicyId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public int getFriendPolicyId() {
		return friendPolicyId;
	}

	public void setFriendPolicyId(int friendPolicyId) {
		this.friendPolicyId = friendPolicyId;
	}

	public int getSafePolicyId() {
		return safePolicyId;
	}

	public void setSafePolicyId(int safePolicyId) {
		this.safePolicyId = safePolicyId;
	}
}

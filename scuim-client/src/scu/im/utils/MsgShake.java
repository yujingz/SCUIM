package scu.im.utils;

import java.awt.Point;

import scu.im.window.FriendUnit;

public class MsgShake extends Thread {

	private String shakerUid;
	private FriendUnit shakerUnit;
	private Point defaultPoint;

	public MsgShake(String uid) {		
		shakerUid = uid;
		shakerUnit = Information.getFriendUnitHash().get(shakerUid);
		Information.getShakingUnit().put(shakerUid, this);
		defaultPoint = shakerUnit.faceLabel.getLocation();
	}

	public void run() {
		while(true){
			try {
				shakerUnit.faceLabel.setLocation( shakerUnit.faceLabel.getLocation().x, shakerUnit.faceLabel.getLocation().y);
				Thread.sleep(390);
				shakerUnit.faceLabel.setLocation(shakerUnit.faceLabel.getLocation().x-3, shakerUnit.faceLabel.getLocation().y+2);
				Thread.sleep(390);
				shakerUnit.faceLabel.setLocation(shakerUnit.faceLabel.getLocation().x+3, shakerUnit.faceLabel.getLocation().y-2);
				Thread.sleep(390);
				shakerUnit.faceLabel.setLocation(shakerUnit.faceLabel.getLocation().x+3, shakerUnit.faceLabel.getLocation().y+2);
				Thread.sleep(390);
				shakerUnit.faceLabel.setLocation(shakerUnit.faceLabel.getLocation().x-3, shakerUnit.faceLabel.getLocation().y-2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
	}
	
	public void stopShake(){
		shakerUnit.faceLabel.setLocation(defaultPoint);
		Information.getShakingUnit().remove(shakerUid);
		this.stop();
	}
}

package fr.statepanel;

public interface HardwareListner {

	void setKeyboardEnabeled(boolean activation);

	void setMouseClicksEnabeled(boolean activation);

	void setMouseMovesEnabeled(boolean activation);

	void setMouseWheelEnabeled(boolean activation);
}

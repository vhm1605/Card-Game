module BaiTapLonNhom5_OOP_TrinhTuanDat {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.media;
	requires javafx.graphics;
	requires java.desktop;

	opens application to javafx.graphics, javafx.fxml;
	opens controller to javafx.fxml;

	exports application;
	exports controller;
}

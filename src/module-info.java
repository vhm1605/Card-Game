module BaiTapLonNhom5_OOP_TrinhTuanDat {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.media;
	requires javafx.graphics;
	requires java.desktop;

	opens application to javafx.graphics, javafx.fxml;
	// Xuất package 'controller' để JavaFX có thể truy cập
	opens controller to javafx.fxml;

	exports controller;
}

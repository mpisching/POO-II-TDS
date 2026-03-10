module br.edu.ifsc.fln.prjjavafxmvc {
    requires javafx.controls;
    requires javafx.fxml;


    opens br.edu.ifsc.fln.prjjavafxmvc to javafx.fxml;
    exports br.edu.ifsc.fln.prjjavafxmvc;

    opens br.edu.ifsc.fln.controller to javafx.fxml;
    exports br.edu.ifsc.fln.controller;

}
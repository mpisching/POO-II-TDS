module br.edu.ifsc.fln.prjexemplojavafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens br.edu.ifsc.fln.prjexemplojavafx to javafx.fxml;
    exports br.edu.ifsc.fln.prjexemplojavafx;
}
package br.edu.ifsc.fln.exception;

/**
 *
 * @author mpisc
 */
public class DAOInsertException extends Exception {

    /**
     * Creates a new instance of <code>DAOInsertException</code> without detail
     * message.
     */
    public DAOInsertException() {
    }

    /**
     * Constructs an instance of <code>DAOInsertException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DAOInsertException(String msg) {
        super(msg);
    }
}

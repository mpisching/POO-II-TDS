package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public abstract class GenericDAO {

    @FunctionalInterface
    protected interface SessionAction<T> {
        T execute(Session session) throws Exception;
    }

    protected <T> T execute(SessionAction<T> action)
            throws DAOException {

        Transaction tx = null;

        try (Session session =
                     HibernateUtil
                             .getSessionFactory()
                             .openSession()) {

            tx = session.beginTransaction();

            T result = action.execute(session);

            tx.commit();

            return result;

        } catch (Exception e) {

            if (tx != null && tx.isActive()) {
                tx.rollback();
            }

            // se já é DAOException → propaga
            if (e instanceof DAOException daoEx) {
                throw daoEx;
            }

            // RuntimeException também propaga
            if (e instanceof RuntimeException runtimeEx) {
                throw runtimeEx;
            }

            throw new DAOException(
                    "Erro na persistência.",
                    e
            );
        }
    }
}
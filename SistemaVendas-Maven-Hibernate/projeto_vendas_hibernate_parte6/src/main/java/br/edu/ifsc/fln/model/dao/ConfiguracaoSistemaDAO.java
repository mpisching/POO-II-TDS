package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.ConfiguracaoSistema;
import br.edu.ifsc.fln.model.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ConfiguracaoSistemaDAO {

    public ConfiguracaoSistema buscarPrimeiro() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                            "FROM ConfiguracaoSistema",
                            ConfiguracaoSistema.class
                    )
                    .setMaxResults(1)
                    .uniqueResult();
        }
    }

    public void atualizar(ConfiguracaoSistema config) {

        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            tx = session.beginTransaction();

            session.merge(config);

            tx.commit();

        } catch (Exception e) {
            if (tx != null && tx.getStatus().canRollback()) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }
}
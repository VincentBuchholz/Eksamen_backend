package facades;

import dtos.RentalDTO;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.List;

public class RentalFacade {
    private static RentalFacade instance;
    private static EntityManagerFactory emf;


    public static RentalFacade getFacadeExample(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new RentalFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }


    public List<RentalDTO> getRentalsByUserID(int userID) {
            EntityManager em = emf.createEntityManager();

            try {
                TypedQuery<RentalDTO> query = em.createQuery("SELECT new dtos.RentalDTO(r) FROM Rental r join Tenant t where t.rentals = r and t.user.id =:userID", RentalDTO.class);
                query.setParameter("userID",userID);
                List<RentalDTO> rentals = query.getResultList();
                return rentals;
            } finally {
                em.close();
            }
        }
}

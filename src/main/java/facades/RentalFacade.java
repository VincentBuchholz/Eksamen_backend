package facades;

import dtos.HouseDTO;
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

    public HouseDTO getHouseByRentalID(int rentalID) {
        EntityManager em = getEntityManager();

        try {
            TypedQuery<HouseDTO> query = em.createQuery("SELECT new dtos.HouseDTO(h) FROM House h join Rental r where h.rentals = r and r.id=:rentalID", HouseDTO.class);
            query.setParameter("rentalID", rentalID);
            query.setMaxResults(1);
            HouseDTO houseDTO = query.getSingleResult();
            return houseDTO;
        } finally {
            em.close();
        }
    }

    public List<HouseDTO> getAllHouses() {
        EntityManager em = getEntityManager();

        try {
            TypedQuery<HouseDTO> query = em.createQuery("SELECT new dtos.HouseDTO(h) FROM House h", HouseDTO.class);
            List<HouseDTO> houseDTOs = query.getResultList();
            return houseDTOs;
        } finally {
            em.close();
        }
    }
}

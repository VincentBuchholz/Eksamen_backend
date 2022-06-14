package facades;

import dtos.HouseDTO;
import dtos.RentalDTO;
import dtos.TenantDTO;
import entities.House;
import entities.Rental;
import entities.Tenant;

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

    public List<RentalDTO> getAllRentals() {
        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<RentalDTO> query = em.createQuery("SELECT new dtos.RentalDTO(r) FROM Rental r", RentalDTO.class);
            List<RentalDTO> rentals = query.getResultList();
            return rentals;
        } finally {
            em.close();
        }
    }

    public RentalDTO getRentalByID(int rentalID) {
        EntityManager em = getEntityManager();
        try{
            Rental rental = em.find(Rental.class, rentalID);
            RentalDTO rentalDTO = new RentalDTO(rental);
            for (Tenant tenant: rental.getTenants()) {
                rentalDTO.addTenant(new TenantDTO(tenant));
            }
            return rentalDTO;
        } finally {
            em.close();
        }
    }

    public RentalDTO updateRentalInfo(RentalDTO rentalDTO) {
        EntityManager em = getEntityManager();
        Rental rental = em.find(Rental.class,rentalDTO.getId());
        rental.setContactPerson(rentalDTO.getContact());
        rental.setStartDate(rentalDTO.getStart());
        rental.setEndDate(rentalDTO.getEnd());
        rental.setPriceAnnual(rentalDTO.getPrice());
        rental.setDeposit(rentalDTO.getDeposit());
        try{
            em.getTransaction().begin();
            em.merge(rental);
            em.getTransaction().commit();
            return new RentalDTO(rental);
        } finally {
            em.close();
        }
    }

    public RentalDTO setHouse(int rentalID, int houseID) {
        EntityManager em = getEntityManager();
        Rental rental = em.find(Rental.class,rentalID);
        House house = em.find(House.class,houseID);
        house.addRental(rental);
        try{
            em.getTransaction().begin();
            em.merge(house);
            em.getTransaction().commit();
            return new RentalDTO(rental);
        } finally {
            em.close();
        }
    }

    public RentalDTO addTenantToRental(int rentalID, int tenantID) {
        EntityManager em = getEntityManager();
        Rental rental = em.find(Rental.class,rentalID);
        Tenant tenant = em.find(Tenant.class,tenantID);
        rental.addTenant(tenant);
        try{
            em.getTransaction().begin();
            em.merge(rental);
            em.getTransaction().commit();
            return new RentalDTO(rental);
        } finally {
            em.close();
        }
    }

    public RentalDTO removeTenantFromRental(int rentalID, int tenantID) {
        EntityManager em = getEntityManager();
        Rental rental = em.find(Rental.class,rentalID);
        Tenant tenant = em.find(Tenant.class,tenantID);
        rental.removeTenant(tenant);
        try{
            em.getTransaction().begin();
            em.merge(rental);
            em.getTransaction().commit();
            return new RentalDTO(rental);
        } finally {
            em.close();
        }
    }
}

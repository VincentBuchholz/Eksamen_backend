package facades;

import dtos.HouseDTO;
import dtos.RentalDTO;
import dtos.TenantDTO;
import entities.House;
import entities.Rental;
import entities.Tenant;
import errorhandling.DateFormatException;
import utils.DateChecker;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.text.ParseException;
import java.util.ArrayList;
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
            query.setParameter("userID", userID);
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
        try {
            Rental rental = em.find(Rental.class, rentalID);
            RentalDTO rentalDTO = new RentalDTO(rental);
            for (Tenant tenant : rental.getTenants()) {
                rentalDTO.addTenant(new TenantDTO(tenant));
            }
            return rentalDTO;
        } finally {
            em.close();
        }
    }

    public RentalDTO updateRentalInfo(RentalDTO rentalDTO) {
        EntityManager em = getEntityManager();
        Rental rental = em.find(Rental.class, rentalDTO.getId());
        rental.setContactPerson(rentalDTO.getContact());
        rental.setStartDate(rentalDTO.getStart());
        rental.setEndDate(rentalDTO.getEnd());
        rental.setPriceAnnual(rentalDTO.getPrice());
        rental.setDeposit(rentalDTO.getDeposit());
        try {
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
        Rental rental = em.find(Rental.class, rentalID);
        House house = em.find(House.class, houseID);
        house.addRental(rental);
        try {
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
        Rental rental = em.find(Rental.class, rentalID);
        Tenant tenant = em.find(Tenant.class, tenantID);
        rental.addTenant(tenant);
        try {
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
        Rental rental = em.find(Rental.class, rentalID);
        Tenant tenant = em.find(Tenant.class, tenantID);
        rental.removeTenant(tenant);
        try {
            em.getTransaction().begin();
            em.merge(rental);
            em.getTransaction().commit();
            return new RentalDTO(rental);
        } finally {
            em.close();
        }
    }

    public RentalDTO deleteRental(int rentalID) {
        EntityManager em = getEntityManager();
        Rental rental = em.find(Rental.class, rentalID);
        try {
            em.getTransaction().begin();
            em.remove(rental);
            em.getTransaction().commit();
            return new RentalDTO(rental);
        } finally {
            em.close();
        }
    }

    public List<TenantDTO> getCurrentTenantsByHouseID(int houseID) throws ParseException {
        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<Rental> query = em.createQuery("SELECT r FROM Rental r where r.house.id=:houseID", Rental.class);
            query.setParameter("houseID", houseID);
            List<Rental> rentals = query.getResultList();
            int currentRentalID = 0;
            for (Rental rental : rentals) {
                if (DateChecker.CHECK_DATES(rental.getStartDate(), rental.getEndDate())) {
                    currentRentalID = rental.getId();
                }
            }
            TypedQuery<TenantDTO> tq = em.createQuery("SELECT new dtos.TenantDTO(t) FROM Tenant t join Rental r where t.rentals = r and r.id=:rentalID", TenantDTO.class);
            tq.setParameter("rentalID", currentRentalID);

            return tq.getResultList();
        } finally {
            em.close();
        }
    }

    public List<RentalDTO> getRentalsByHouseID(int houseID) {
        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<RentalDTO> query = em.createQuery("SELECT new dtos.RentalDTO(r) FROM Rental r Where r.house.id=:houseID", RentalDTO.class);
            query.setParameter("houseID", houseID);
            List<RentalDTO> rentals = query.getResultList();
            return rentals;
        } finally {
            em.close();
        }
    }

    public RentalDTO createRental(RentalDTO rentalDTO) throws DateFormatException {
        EntityManager em = getEntityManager();
        if(!DateChecker.CORRECT_FORMAT(rentalDTO.getStart())){
            throw new DateFormatException("Date format must be dd/MM/yyyy");
        }

        if(!DateChecker.CORRECT_FORMAT(rentalDTO.getEnd())){
            throw new DateFormatException("Date format must be dd/MM/yyyy");
        }

        Rental newRental = new Rental(rentalDTO.getStart(), rentalDTO.getEnd(), rentalDTO.getPrice(), rentalDTO.getDeposit(), rentalDTO.getContact());

        try{
            House house = em.find(House.class,rentalDTO.getHouseID());
            Tenant tenant = em.find(Tenant.class,rentalDTO.getTenantID());
            newRental.setHouse(house);
            newRental.addTenant(tenant);
            em.getTransaction().begin();
            em.persist(newRental);
            em.getTransaction().commit();
            return new RentalDTO(newRental);
        } finally {
            em.close();
        }
    }
}

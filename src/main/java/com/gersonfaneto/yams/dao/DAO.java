package com.gersonfaneto.yams.dao;

import com.gersonfaneto.yams.dao.entities.client.ClientCRUD;
import com.gersonfaneto.yams.dao.entities.client.ClientMemoryDAO;
import com.gersonfaneto.yams.dao.entities.receptionist.ReceptionistCRUD;
import com.gersonfaneto.yams.dao.entities.receptionist.ReceptionistMemoryDAO;
import com.gersonfaneto.yams.dao.entities.technician.TechnicianCRUD;
import com.gersonfaneto.yams.dao.entities.technician.TechnicianMemoryDAO;

public abstract class DAO {
    private static ClientCRUD clientCRUD;
    private static ReceptionistCRUD receptionistCRUD;
    private static TechnicianCRUD technicianCRUD;

    public static ClientCRUD fromClients() {
        if (clientCRUD == null) {
            clientCRUD = new ClientMemoryDAO();
        }

        return clientCRUD;
    }

    public static ReceptionistCRUD fromReceptionists() {
        if (receptionistCRUD == null) {
            receptionistCRUD = new ReceptionistMemoryDAO();
        }

        return receptionistCRUD;
    }

    public static TechnicianCRUD fromTechnicians() {
        if (technicianCRUD == null) {
            technicianCRUD = new TechnicianMemoryDAO();
        }

        return technicianCRUD;
    }
}

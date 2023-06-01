package com.gersonfaneto.yams.controllers;

import com.gersonfaneto.yams.dao.DAO;
import com.gersonfaneto.yams.dao.Persist;
import com.gersonfaneto.yams.models.entities.user.User;

public abstract class MainController {
  public static User loggedUser;

  public static void saveData() {
    ((Persist) DAO.fromUsers()).saveAll();
    ((Persist) DAO.fromUsers()).saveAll();
    ((Persist) DAO.fromPayments()).saveAll();
    ((Persist) DAO.fromComponents()).saveAll();
    ((Persist) DAO.fromService()).saveAll();
    ((Persist) DAO.fromPurchaseOrders()).saveAll();
    ((Persist) DAO.fromWorkOrders()).saveAll();
  }

  public static void loadData() {
    ((Persist) DAO.fromUsers()).loadAll();
    ((Persist) DAO.fromUsers()).loadAll();
    ((Persist) DAO.fromPayments()).loadAll();
    ((Persist) DAO.fromComponents()).loadAll();
    ((Persist) DAO.fromService()).loadAll();
    ((Persist) DAO.fromPurchaseOrders()).loadAll();
    ((Persist) DAO.fromWorkOrders()).loadAll();
  }
}

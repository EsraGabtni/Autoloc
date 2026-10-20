package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.domain.Paiement;

/**
 * Lecture seule : la création et la suppression d'un paiement passent par le Contrat.
 */
public interface IPaiementService {

    Paiement findById(Long id);

    List<Paiement> findAll();
}

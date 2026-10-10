# Stratégie de fetch et de cascade — AutoLoc

Règles retenues :
- **Fetch LAZY partout** : une association ne se charge que lorsqu'on l'utilise. Les `@ManyToOne` et `@OneToOne` sont EAGER par défaut, on les force donc en LAZY pour ne pas charger toute la chaîne d'objets liés.
- **Cascade uniquement en cas de composition** (l'enfant ne peut pas vivre sans son parent). Sinon aucune cascade.
- Le **côté propriétaire** porte la clé étrangère ; le côté inverse utilise `mappedBy`.

| Association | Côté propriétaire | Fetch | Cascade | Justification |
|---|---|---|---|---|
| Contrat → Paiement | Paiement (`contrat_id_contrat`) | LAZY | ALL + orphanRemoval | Un paiement n'existe que rattaché à son contrat : supprimer le contrat supprime ses paiements, retirer un paiement de la liste le supprime en base. |
| Agence → Vehicule | Vehicule (`id_agence`) | LAZY | Aucune | Un véhicule survit à la suppression de son agence (il peut être réaffecté). |
| Agence → Employe | Employe (`id_agence`) | LAZY | Aucune | Un employé a une existence propre ; on ne le supprime pas avec son agence. |
| Vehicule ↔ Equipement | Vehicule (table `vehicule_equipement`) | LAZY | Aucune | Les équipements sont partagés entre véhicules : supprimer un véhicule ne doit pas supprimer un équipement utilisé par d'autres. On utilise un `Set` pour éviter les doublons et les requêtes inutiles. |
| Client → Reservation | Reservation (`id_client`) | LAZY | PERSIST | Enregistrer un client avec de nouvelles réservations les enregistre aussi, mais on ne supprime pas l'historique avec le client. |
| Reservation → Vehicule | Reservation (`id_vehicule`) | LAZY | Aucune | Le véhicule existe indépendamment de ses réservations. Association unidirectionnelle. |
| Reservation ↔ Contrat | Contrat (`id_reservation`) | LAZY | ALL (côté Reservation) | Un contrat est issu d'une réservation et n'a pas de sens sans elle. |
| Vehicule → Maintenance | Maintenance (`id_vehicule`) | LAZY | PERSIST | Une maintenance enregistrée avec le véhicule est sauvegardée en même temps, mais elle reste dans l'historique si on la retire de la liste. |

## Remarques
- Les entités utilisent `@Getter`/`@Setter` ciblés et **pas `@Data`** : `toString`/`hashCode` parcourraient les deux côtés d'une association bidirectionnelle (risque de `StackOverflowError`).
- `@OneToOne(mappedBy = ...)` en LAZY n'est pas toujours respecté par Hibernate côté inverse sans amélioration du bytecode : la requête de chargement peut quand même être exécutée.
- Accéder à une collection LAZY hors transaction lève une `LazyInitializationException` : on utilisera des requêtes dédiées (Atelier 7).

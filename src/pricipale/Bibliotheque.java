package pricipale;
    public void afficherDocument(){
        System.out.println("\n\t *****\tLes documents de la bibliotheque\t*****");
        int  nb=0;
        if (listDocument.size() == 0)
            System.out.println("\t Il n'y a pas encore de document enregistrer dans cette bibliotheque");
        else{
            enteteDocument();
            for (Document d:listDocument){
                int disponible = d.getNbExemplaires()-d.getListPret().size();
                d.afficherCeDocument(disponible, nombreDePretTotale);
                cadreDocument();
                nb++;
            }
        }
        System.out.println("\n\t "+nb+" Document disponible! Appuyez sur entrer pour continuer...");
        sc.nextLine();
    }

    public void ajouterDocument(){
        System.out.println("\n\t *****\tAjouter un nouveau document a la bibliotheque\t*****");
        String choixType, str;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println("\t Veillez selectionner le type du document a ajouter");
        do {
            System.out.println("\t 0) Annuler");
            System.out.println("\t 1) Article Scientifique");
            System.out.println("\t 2) Livre");
            System.out.println("\t 3) Magazine Scientifique");
            choixType = sc.nextLine();
            switch (choixType){
                case "0": gestionDocument(); break;
                case "1":
                    Article a = new Article();
                    remplissageDocument(a);
                    System.out.println("\t Entrer Nom d'auteur");
                    a.setNomAuteur(sc.nextLine());
                    System.out.println("\t Entrer la date de publication de l'article sous forme (DD/MM/YYYY)");
                    str = sc.nextLine();
                    boolean verifier = false;
                    do {
                        try {
                            a.setDatePublication(sdf.parse(str));
                            verifier = true;
                        }catch (ParseException e){
                            System.out.println("\t Forme non respecter!");
                            System.out.println("\t Entrer la date de publication de l'article sous forme (DD/MM/YYYY)");
                            str = sc.nextLine();
                        }
                    }while (!verifier);
                    this.listDocument.add(a);
                    historique.add(new Historique(new Date(), "Document", "Ajout",a.getId(), "Article : "+a.getTitre()+" par "+a.getNomAuteur()));
                    this.id_document++;
                    System.out.println("\n\t Article ajouté avec succées.\n");
                    pause(1000);
                    return;
                case "2":
                    Livre l = new Livre();
                    remplissageDocument(l);
                    System.out.println("\t Entrer Nom d'auteur");
                    l.setNomAuteur(sc.nextLine());
                    System.out.println("\t Entrer Nom éditeur");
                    l.setNomEditeur(sc.nextLine());
                    System.out.println("\t Entrer la date de l'édition du livre sous forme (DD/MM/YYY)");
                    str = sc.nextLine();
                    verifier = false;
                    do {
                        try {
                            l.setDateEdition(sdf.parse(str));
                            verifier = true;
                        }catch (ParseException e){
                            System.out.println("\t Forme non respecter!");
                            System.out.println("\t Entrer la date de l'édition du livre sous forme (DD/MM/YYY)");
                            str = sc.nextLine();
                        }
                    }while (!verifier);
                    this.listDocument.add(l);
                    historique.add(new Historique(new Date(), "Document", "Ajout",l.getId(), "Livre : "+l.getTitre()+" par "+l.getNomAuteur()));
                    this.id_document++;
                    System.out.println("\n\t Livre ajouté avec succées.\n");
                    pause(1000);
                    return;
                case "3":
                    Magazine m = new Magazine();
                    verifier = false;
                    remplissageDocument(m);
                    System.out.println("\t Entree la fréquence de parution du magazin");
                    do {
                        try {
                            m.setFrequence(sc.nextInt());
                            verifier = true;
                        }
                        catch (Exception e){
                            System.out.println("\t Erreur! Veillez entrer des chiffres...");
                            System.out.println("\t Entree la fréquence de parution du magazin");
                            pause(500);
                            sc.nextLine();
                        }
                    }while (!verifier);
                    sc.nextLine();
                    this.listDocument.add(m);
                    historique.add(new Historique(new Date(), "Document", "Ajout",m.getId(), "Magazine : "+m.getTitre()));
                    this.id_document++;
                    System.out.println("\n\t Magazine ajouté avec succées.\n");
                    pause(1000);
                    return;
                default:
                    System.out.println("\t Choix du type Document est non valide veillez réessayer");
                    pause(1000);
                    break;
            }
        }while (!choixType.equals("0"));
    }

    public void modifierDocument(){
        System.out.println("\n\t *****\tModifier un document par id\t*****");
        Document d;
        d = chercherDocumentById();
        d.modifierCeDocument(sc, historique);
    }

    public void supprimerDocument(){
        System.out.println("\n\t *****\tSuppression de document\t*****");
        Document d;
        d = chercherDocumentById();
        System.out.println("\t Vous êtes sur de supprimer le document : "+d.getTitre()+" ?");
        if (d.getListPret().size() !=0)
            System.out.println("\n\t !! Ce document possède des pret en cours, ca suppression évoquera la suppression de tout ces prets !!");
        System.out.println("\t 1 ) Continuer");
        if (sc.nextLine().equals("1")){
            historique.add(new Historique(new Date(), "Document", "Suppression",d.getId(), d.getTitre()));
            listDocument.remove(d);
            System.out.println("\t Document supprimer avec succees...");
            pause(1000);
            return;
        }else
            System.out.println("\t Annulation...");
        pause(1000);
    }

    public void chercherDocument(){
        System.out.println("\n\t *****\tChercher un document et afficher ces prets\t*****");
        ArrayList<Document> listDocumentTrouver = new ArrayList<>();
        chercherDocByIdOrTitre(listDocumentTrouver);
        enteteDocument();
        for (Document d:listDocumentTrouver){
            int disponible = d.getNbExemplaires()-d.getListPret().size();
            d.afficherCeDocument(disponible, nombreDePretTotale);
            cadreDocument();
        }
        pause(500);
        if (listDocumentTrouver.size() == 1)
            System.out.println("\n\t Les prêts en cours de "+listDocumentTrouver.get(0).getTitre()+" : \n");
        else
            System.out.println("\n\t Les prêts en cours de ces documents : \n");
        entetePret();
        for (Document d:listDocumentTrouver)
            for (PretDocument pretDocument:d.getListPret()){
                pretDocument.afficherCePret(d);
                cadrePret();
            }
        System.out.println("\t Appuyez sur entrer pour continuer...");
        sc.nextLine();
    }

    public void gestionPret(){
        String choixPret;
        do {
            System.out.println("\n\t *****\tGestion Des Prets\t*****");
            System.out.println("\t Veillez choisir le numero correspondant à votre choix");
            System.out.println("\t 0 ) Retour au menu principale");
            System.out.println("\t 1 ) Afficher tout Prêts disponible");
            System.out.println("\t 2 ) Ajouter un nouveau prêt");
            System.out.println("\t 3 ) Modifier un prêt");
            System.out.println("\t 4 ) Rendre un document");
            choixPret = sc.nextLine();
            switch (choixPret){
                case "0": menu(); break;
                case "1": afficherPret(); break;
                case "2": ajouterPret(); break;
                case "3": modifierPret(); break;
                case "4": rendrePret(); break;
                default: System.out.println("\t Choix invalide! veillez réessayer"); pause(1000); break;
            }
        }while (!choixPret.equals("0"));
    }

    public void afficherPret(){
        int nb=0;
        System.out.println("\n\t ******\tTout les prêts en cours\t*****");
//        VERIFIER SI IL EXISTE UN PRET EN COURS
        for (Document d:listDocument)
            for (PretDocument pretDocument:d.getListPret()) {
                if (nb==0){
                    entetePret();
                }
                pretDocument.afficherCePret(d);
                cadrePret();
                nb++;
            }
        if (nb==0)
            System.out.println("\t Il n'y a aucune pret en cours!");
        System.out.println("\n\t "+nb+" pret disponible! Appuyez sur entrer pour continuer...");
        sc.nextLine();
    }

    public void ajouterPret(){
        System.out.println("\n\t *****\tAjouter un prêt\t*****");
        System.out.println("\t Selectionnez l'adherent");
        Adherent a;
        a = chercherAdherentById();
        System.out.println("\t Selectionnez le document");
        Document d;
        d = chercherDocumentById();
//      VOIR SI RETARDATAIRE
        if (!a.isRetard()){
//          ADHERENT N'EST PAS RETARDATAIRE & VOIRE S'IL A ATTEINT CES NOMBRE DE PRET LIMITE
            if (a.getNbMaxPrets()>a.getNbPret()){
//              TOUTES LES CONTRAINTES SUR L'ADHERENT SONT VERIFIER & VOIR SI DOCUMENT EXISTE ENCORE DANS LA BIBLIOTHEQUE
                if (d.getListPret().size()<d.getNbExemplaires()){
//                  TOUT EST VERIFIER & MAINTENANT NE RESTE QUE L'AFFECTATION
                    PretDocument pretDocument = new PretDocument();
                    pretDocument.setDateDuPret(new Date());
                    pretDocument.setAdherent(a);
                    pretDocument.calculerDateRetour();
//                    AJOUTER LE PRET
                    d.getListPret().add(pretDocument);
                    a.setNbPret(a.getNbPret()+1);
                    d.setNbPretTotale(d.getNbPretTotale()+1);
                    a.setNbPretTotale(a.getNbPretTotale()+1);
                    historique.add(new Historique(new Date(),  "Pret", "Ajout", a.getId(), a.getType()+": "+a.getPrenom()+" "+a.getNom()+" a pris le document : "+d.getTitre()));
                    System.out.println("\t Pret du document effectuée avec succées! MERCI.");
                    nombreDePretTotale++;
                    pause(1000);
                    return;
                }
                System.out.println("\t Ce document n'est plus disponible!");
                pause(1000);
                return;
            }
            System.out.println("\t Cette adherent a atteint ces emprint limite");
            pause(1000);
            return;
        }
        System.out.println("\t Cette adherent est retardataire et ne peux plus enpreter de documents!");
        pause(1000);
    }

    public void modifierPret(){
        System.out.println("\n\t *****\tModification d'un prêt\t*****");
        String choix;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println("\t Selectionnez l'adherent");
        Adherent a;
        a = chercherAdherentById();
        System.out.println("\t Selectionnez le document");
        Document d;
        d = chercherDocumentById2(a);
        for (PretDocument pretDocument:d.getListPret()){
            if (pretDocument.getAdherent() == a){
//                MODIFIER
                do {
                    System.out.println("\t Veillez choisir le numero correspondant à votre choix");
                    System.out.println("\t 0 ) Annuler");
                    System.out.println("\t 1 ) Modifier le document ("+d.getTitre()+")");
                    System.out.println("\t 2 ) Modifier l'adherent ("+a.getPrenom()+" "+a.getNom()+")");
                    System.out.println("\t 3 ) Modifier la date du pret ("+sdf.format(pretDocument.getDateDuPret())+")");
                    choix = sc.nextLine();
                    switch (choix){
                        case "0": gestionPret(); break;
                        case "1":
                            Document newDocument;
                            System.out.println("\t Chercher le nouveau document");
                            newDocument = chercherDocumentById();
//                        VERIFIER SI LE NOUVEAU DOCUMENT EXISTE
                            if (newDocument.getListPret().size()<newDocument.getNbExemplaires()){
//                              TOUT EST VERIFIER & MAINTENANT NE RESTE QUE L'AFFECTATION
                                newDocument.getListPret().add(pretDocument);
                                d.getListPret().remove(pretDocument);
                                newDocument.setNbPretTotale(newDocument.getNbPretTotale()+1);
                                d.setNbPretTotale(d.getNbPretTotale()-1);
                                historique.add(new Historique(new Date(), "Pret", "Modification", 0, "Document: Adherent "+a.getPrenom()+" "+a.getNom()+" change le document"+d.getTitre()+" par "+newDocument.getTitre()));
                                d = newDocument;
                                System.out.println("\t Modification Réusite.");
                                pause(500);
                                break;
                            }
                            System.out.println("\t Ce nouveau document n'est plus disponible!");
                            pause(500);
                            break;
                        case "2":
                            Adherent newAdherent;
                            System.out.println("\t Chercher le nouveau adherent");
                            newAdherent = chercherAdherentById();
//                            TESTER SI ADHERENT RETARDATAIRE OU NE PEUX PLUS PRENDRE DE DOCUMENTS
                            if (!newAdherent.isRetard())
                                if (newAdherent.getNbMaxPrets()>newAdherent.getNbPret()){
                                    pretDocument.setAdherent(newAdherent);
                                    newAdherent.setNbPret(newAdherent.getNbPret()+1);
                                    a.setNbPret(a.getNbPret()-1);
                                    newAdherent.setNbPretTotale(newAdherent.getNbPretTotale()+1);
                                    a.setNbPretTotale(a.getNbPretTotale()-1);
                                    historique.add(new Historique(new Date(), "Pret", "Modification", 0, "Adherent: "+a.getType()+": "+a.getPrenom()+" "+a.getNom()+" par "+newAdherent.getType()+": "+newAdherent.getPrenom()+" "+newAdherent.getNom()));
                                    a = newAdherent;
                                    System.out.println("\t Modification réussite.");
                                    pause(500);
                                    break;
                                }
                            System.out.println("\t Cet Adherent ne peut pas empreinter un document!");
                            pause(500);
                            break;
                        case "3":
//                            MODIFICATION DE LA DATE DE PRET
                            String str, ancienneDate;
                            ancienneDate = sdf.format(pretDocument.getDateDuPret()) ;
                            System.out.println("\t Entrer la date de publication de l'article sous forme (DD/MM/YYYY)");
                            str = sc.nextLine();
                            try {
                                pretDocument.setDateDuPret(sdf.parse(str));
                                pretDocument.calculerDateRetour();
                                historique.add(new Historique(new Date(), "Pret", "Modification", 0, "Date: de "+ancienneDate+" a "+sdf.format(pretDocument.getDateDuPret())));
                                System.out.println("\t Modification réussite.");
                                pause(500);
                            }catch (ParseException e){
                                System.out.println("\t Forme non respecter! ");
                                pause(500);
                            }
                            break;
                        default:
                            System.out.println("\t Choix non valide veillez réessayer");
                            pause(500);
                            break;
                    }
                }while (!choix.equals("0"));
            }
        }
        System.out.println("\t Cette adherent na pas empreté ce document");
    }

    public void rendrePret(){
        System.out.println("\n\t *****\tRendre un document\t*****");
        System.out.println("\t Chercher l'adherent a rendre le document");
        Adherent a;
        a = chercherAdherentById();
        System.out.println("\t Chercher le document a rendre");
        Document d;
        d = chercherDocumentById2(a);
        for (PretDocument pretDocument:d.getListPret()){
            if (pretDocument.getAdherent() == a){
                historique.add(new Historique(new Date(), "Pret", "Suppression", a.getId(), "l 'adherent: "+a.getPrenom()+" "+a.getNom()+" a rendu le document: "+d.getTitre()));
                d.getListPret().remove(pretDocument);
                a.setNbPret(a.getNbPret()-1);
                System.out.println("\t Document rendu avec succées. Merci!");
                pause(500);
                return;
            }
        }
        System.out.println("\t Cette adherent n'a pas empreter ce document");
    }

    public void toutHistorique(){
        System.out.println("\n\t ***\tHISTORIQUE\t*****");
        String choix;
        do {
            System.out.println("\t Veillez choisir le numero correspondant à votre choix");
            System.out.println("\t 0 ) Retour au menu principale");
            System.out.println("\t 1 ) Afficher tout l'historique disponible");
            System.out.println("\t 2 ) Afficher historique des adherents");
            System.out.println("\t 3 ) Afficher historique des documents");
            System.out.println("\t 4 ) Afficher historique des prets");
            System.out.println("\t 5 ) Afficher historique des modifications");
            System.out.println("\t 6 ) Afficher historique des ajout");
            System.out.println("\t 7 ) Afficher historique des suppressions");
            System.out.println("\t 8 ) Afficher historique d'une date entrer");
            System.out.println("\t 9 ) Afficher historique par ID");
            choix = sc.nextLine();
            switch (choix){
                case "0": menu(); break;
                case "1": afficherHistorique(); break;
                case "2": afficherHistoriqueByClassName("Adherent"); break;
                case "3": afficherHistoriqueByClassName("Document"); break;
                case "4": afficherHistoriqueByClassName("Pret"); break;
                case "5": afficherHistoriqueByType("Modification"); break;
                case "6": afficherHistoriqueByType("Ajout"); break;
                case "7": afficherHistoriqueByType("Suppression"); break;
                case "8": afficherHistoriqueByDate(); break;
                case "9": afficherHistoriqueByID(); break;
                default: System.out.println("\t Choix invalide! veillez réessayer"); pause(1000); break;
            }
        }while (!choix.equals("0"));
    }

    public void afficherHistorique(){
        int count =historique.size()-1;
        int i;
        System.out.println("\n\t *****\tHISTORIQUE DE LA BIBLIOTHEQUE\t*****\n");
        enteteHistorique();
        outerloop:
        while (count>=0) {
            i=0;
            while (i < 10) {
                if (count >= 0) {
                    historique.get(count).afficherCetHistorique();
                    i++;
                    count--;
                } else
                    break outerloop;
            }
            if (count >=0){
                System.out.println("\t entrer pour continuer, 0 pour sortire...");
                if (sc.nextLine().equals("0"))
                    return;
            }
        }
        System.out.println("\n\t Appuyer sur entrer pour retourner...");
        sc.nextLine();
    }

    public void afficherHistoriqueByClassName(String str){
        int count =historique.size()-1;
        int i;
        System.out.println("\n\t *****\tHISTORIQUE DE LA BIBLIOTHEQUE\t*****\n");
        enteteHistorique();
        outerloop:
        while (count>=0) {
            i=0;
            while (i < 10) {
                if (count >= 0) {
                    if (historique.get(count).getClasse().equals(str)){
                        historique.get(count).afficherCetHistorique();
                        i++;
                    }
                    count--;
                } else
                    break outerloop;
            }
            if (count >=0){
                System.out.println("\t entrer pour continuer, 0 pour sortire...");
                if (sc.nextLine().equals("0"))
                    return;
            }
        }
        System.out.println("\n\t Appuyer sur entrer pour retourner...");
        sc.nextLine();
    }

    public void afficherHistoriqueByType(String str){
        int count =historique.size()-1;
        int i=0;
        System.out.println("\n\t *****\tHISTORIQUE DE LA BIBLIOTHEQUE\t*****\n");
        enteteHistorique();
        outerloop:
        while (count>=0) {
            while (i < 10) {
                i=0;
                if (count >= 0) {
                    if (historique.get(count).getType().equals(str)){
                        historique.get(count).afficherCetHistorique();
                        i++;
                    }
                    count--;
                } else
                    break outerloop;
            }
            if (count >=0){
                System.out.println("\t entrer pour continuer, 0 pour sortire...");
                if (sc.nextLine().equals("0"))
                    return;
            }
        }
        System.out.println("\n\t Appuyer sur entrer pour retourner...");
        sc.nextLine();
    }

    public void afficherHistoriqueByDate(){
        SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
        String str;
        int i;
        System.out.println("\t Entrer une date sous forme (dd/MM/yyyy) : ");
        str = sc.nextLine();
        try {
            sdf.parse(str);
        }catch (ParseException e){
            System.out.println("\t Forme non respecter...");
            pause(500);
            return;
        }
        int count =historique.size()-1;
        System.out.println("\n\t *****\tHISTORIQUE DE LA BIBLIOTHEQUE\t*****\n");
        enteteHistorique();
        outerloop:
        while (count>=0) {
            i=0;
            while (i < 10) {
                if (count >= 0) {
                    if (sdf.format(historique.get(count).getDateAjout()).equals(str)){
                        historique.get(count).afficherCetHistorique();
                        i++;
                    }
                    count--;
                } else
                    break outerloop;
            }
            if (count >=0){
                System.out.println("\t entrer pour continuer, 0 pour sortire...");
                if (sc.nextLine().equals("0"))
                    return;
            }
        }
        System.out.println("\n\t Appuyer sur entrer pour retourner...");
        sc.nextLine();
    }

    public void afficherHistoriqueByID(){
        Adherent a;
        a = chercherAdherentById();
        int i;
        int count =historique.size()-1;
        System.out.println("\n\t *****\tHISTORIQUE DE LA BIBLIOTHEQUE\t*****\n");
        enteteHistorique();
        outerloop:
        while (count>=0) {
            i=0;
            while (i < 10) {
                if (count >= 0) {
                    if (historique.get(count).getId() == a.getId() && historique.get(count).getClasse().equals("Adherent")){
                        historique.get(count).afficherCetHistorique();
                        i++;
                    }
                    count--;
                } else
                    break outerloop;
            }
            if (count >=0){
                System.out.println("\t entrer pour continuer, 0 pour sortire...");
                if (sc.nextLine().equals("0"))
                    return;
            }
        }
        System.out.println("\n\t Appuyer sur entrer pour retourner...");
        sc.nextLine();
    }

    public Adherent chercherAdherentById(){
        int id;
        String choix;
        Adherent adherent = new Adherent();
        do {
            System.out.println("\t 0 ) Annuler");
            System.out.println("\t 1 ) Lister les adherents");
            System.out.println("\t 2 ) Entree ID adherent");
            System.out.println("\t Veillez choisir une option : ");
            choix = sc.nextLine();
            switch (choix){
                case "0": gestionAdherent(); break;
                case "1": afficherAdherent(); break;
                case "2":
                    System.out.println("\t Entrer l'ID de l'adherent : ");
                    id = verifierID();
                    sc.nextLine();
                    for (Adherent a:listAdherent)
                        if (a.getId() == id)
                            return a;
                    System.out.println("\t Adherent introuvable! veillez réssayer");
                    pause(500);
                    break;
                default:
                    System.out.println("\t Choix invalide! veillez réessayer...");
                    pause(500);
                    break;
            }
        }while (!choix.equals("0"));
        return adherent;
    }

    public void chercherAdherentByIdOrNameOrAdresse(ArrayList<Adherent> listAdherentTrouve){
        String choix, nom, prenom, adresse;
        int id;
        do {
            System.out.println("\t 0 ) Annuler");
            System.out.println("\t 1 ) Chercher adherent par ID");
            System.out.println("\t 2 ) Chercher adherent par NOM");
            System.out.println("\t 3 ) Chercher adherent par PRENOM");
            System.out.println("\t 4 ) Chercher adherent par ADRESSE");
            System.out.println("\t Veillez choisir une option : ");
            choix = sc.nextLine();
            switch (choix){
                case "0": gestionAdherent(); return;
                case "1":
                    System.out.println("\t Entrer l'ID de l'adherent : ");
                    id = verifierID();
                    sc.nextLine();
                    for (Adherent a:listAdherent)
                        if (a.getId() == id){
                            listAdherentTrouve.add(a);
                            return;
                        }
                    System.out.println("\t Adherent introuvable! veillez réssayer");
                    pause(500);
                    break;
                case "2":
                    System.out.println("\t Entrer NOM adherent");
                    nom = sc.nextLine();
                    for (Adherent a:listAdherent)
                        if (a.getNom().equalsIgnoreCase(nom))
                            listAdherentTrouve.add(a);
                    if (listAdherentTrouve.size() == 0){
                        System.out.println("\t Il n'y a aucun adherent trouver avec ce nom...");
                        pause(500);
                        break;
                    }
                    return;
                case "3":
                    System.out.println("\t Entrer PRENOM adherent");
                    prenom = sc.nextLine();
                    for (Adherent a:listAdherent)
                        if (a.getPrenom().equalsIgnoreCase(prenom))
                            listAdherentTrouve.add(a);
                    if (listAdherentTrouve.size() == 0){
                        System.out.println("\t Il n'y a aucun adherent trouver avec ce prenom...");
                        pause(500);
                        break;
                    }
                    return;
                case "4":
                    System.out.println("\t Entrer ADRESSE adherent");
                    adresse = sc.nextLine();
                    for (Adherent a:listAdherent)
                        if (a.getAdresse().equalsIgnoreCase(adresse))
                            listAdherentTrouve.add(a);
                    if (listAdherentTrouve.size() == 0){
                        System.out.println("\t Il n'y a aucun adherent trouver avec cette adresse...");
                        pause(500);
                        break;
                    }
                    return;
                default:
                    System.out.println("\t Choix invalide! veillez réessayer...");
                    pause(500);
                    break;
            }
        }while (true);
    }

    public void chercherDocByIdOrTitre(ArrayList<Document> listDocumentTrouve){
        int id;
        String choix, titre;
        do {
            System.out.println("\t 0 ) Annuler");
            System.out.println("\t 1 ) Chercher Document par ID");
            System.out.println("\t 2 ) Chercher Document par Titre");
            System.out.println("\t Veillez choisir une option : ");
            choix = sc.nextLine();
            switch (choix){
                case "0": gestionDocument(); return;
                case "1":
                    System.out.println("\t Entrer ID : ");
                    id = verifierID();
                    sc.nextLine();
                    for (Document d:listDocument)
                        if (d.getId() == id){
                            listDocumentTrouve.add(d);
                            return;
                        }
                    System.out.println("\t Document introuvable! veillez réssayer");
                    pause(500);
                    break;
                case "2":
                    System.out.println("\t Entrer Titre : ");
                    titre = sc.nextLine();
                    for (Document d:listDocument)
                        if (d.getTitre().equalsIgnoreCase(titre))
                            listDocumentTrouve.add(d);
                    if (listDocumentTrouve.size() == 0){
                        System.out.println("\t Il n'y a aucun Document trouver avec ce titre...");
                        pause(500);
                        break;
                    }
                    return;
                default:
                    System.out.println("\t Choix invalide! veillez réessayer...");
                    pause(500);
                    break;
            }
        }while (true);
    }

    public Document chercherDocumentById(){
        int id;
        String choix;
        Document document = new Article();
        do {
            System.out.println("\t 0 ) Annuler");
            System.out.println("\t 1 ) Lister les documents disponible");
            System.out.println("\t 2 ) Entree ID document");
            System.out.println("\t Veillez choisir une option : ");
            choix = sc.nextLine();
            switch (choix){
                case "0": gestionDocument(); break;
                case "1": afficherDocument(); break;
                case "2":
                    System.out.println("\t Entrer l'ID du document : ");
                    id = verifierID();
                    sc.nextLine();
                    for (Document d:listDocument){
                        if (d.getId() == id)
                            return d;
                    }
                    System.out.println("\t Document introuvable! veillez réssayer");
                    pause(500);
                    break;
                default:
                    System.out.println("\t Choix invalide! veillez réessayer...");
                    pause(500);
                    break;
            }
        }while (!choix.equals("0"));
        return document;
    }

    public Document chercherDocumentById2(Adherent a){
        int id, nb=0;
        String choix;
        Document document = new Article();
        boolean trouver = false;
        do {
            System.out.println("\t 0 ) Annuler");
            System.out.println("\t 1 ) Lister les documents disponible pour "+a.getPrenom()+" "+a.getNom());
            System.out.println("\t 2 ) Entree ID document");
            System.out.println("\t Veillez choisir une option : ");
            choix = sc.nextLine();
            switch (choix){
                case "0": gestionPret(); break;
                case "1":
                    enteteDocument();
                    for (Document d:listDocument)
                        for (PretDocument pretDocument:d.getListPret())
                            if (pretDocument.getAdherent() == a){
                                int disponible = d.getNbExemplaires()-d.getListPret().size();
                                d.afficherCeDocument(disponible, nombreDePretTotale);
                                cadreDocument();
                                trouver = true;
                                nb++;
                            }
                    if (!trouver)
                        System.out.println("\t Cette adherent n'a pas de document en cours de pret");
                    else
                        System.out.println("\t "+nb+" Document preté pour "+a.getPrenom()+" "+a.getNom());
                    pause(500);
                    break;
                case "2":
                    System.out.println("\t Entrer l'ID du document : ");
                    id = verifierID();
                    sc.nextLine();
                    for (Document d:listDocument){
                        if (d.getId() == id)
                            return d;
                    }
                    System.out.println("\t Document introuvable! veillez réssayer");
                    pause(500);
                    break;
                default:
                    System.out.println("\t Choix invalide! veillez réessayer...");
                    pause(500);
                    break;
            }
        }while (!choix.equals("0"));
        return document;
    }

    public int verifierID() {
        int id;
        do {
            try{
                id = sc.nextInt();
                return id;
            } catch (Exception e){
                System.out.println("\t Erreur! Veillez entrer des chiffres...");
                System.out.println("\t Entrer l'ID une autre fois : ");
                pause(500);
                sc.nextLine();
            }
        }while (true);
    }

    public void remplissageDocument(Document d){
        d.setId(id_document);
        System.out.println("\t Entrer le titre");
        d.setTitre(sc.nextLine());
        System.out.println("\t Entrer localisation (Salle/Rayon)");
        d.setLocalisation(sc.nextLine());
        System.out.println("\t Entrer nombre exemplaires");
        boolean verifier=false;
        do {
            try{
                d.setNbExemplaires(sc.nextInt());
                verifier = true;
            } catch (Exception e){
                System.out.println("\t Erreur! Veillez entrer des chiffres...");
                System.out.println("\t Entrer nombre exemplaires");
                pause(500);
                sc.nextLine();
            }
        }while (!verifier);
        sc.nextLine();
    }

    public void pause(int timeToPause){
        try
        {
            Thread.sleep(timeToPause);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }

    public void cadreAdherent(){
        System.out.print("+-----------");
        System.out.print("+----");
        for (int i=0; i<2; i++)
            System.out.print("+--------------------");
        System.out.print("+------------------------------");
        for (int i=0; i<2; i++)
            System.out.print("+--------");
        System.out.print("+-------------");
        System.out.println("+");
    }

    public void enteteAdherent(){
        cadreAdherent();
        System.out.println("|   "+ Y +"TYPE"+ RES +"    | "+ Y +"ID"+ RES +
                " |        "+ Y +"NOM"+ RES +"         |       "+ Y +"PRENOM"+ RES +
                "       |           "+ Y +"ADRESSE"+ RES +"            |" +
                " "+ Y +"PRETS"+ RES +"  | "+ Y +"RETARD"+ RES +" | "+ Y +"PRET TOTALE"+ RES +" |");
        cadreAdherent();
    }

    public void cadreDocument(){
        System.out.print("+---------");
        System.out.print("+----");
        System.out.print("+------------------------------------");
        for (int i=0; i<2; i++)
            System.out.print("+--------------------");
        System.out.print("+----------");
        for (int i=0; i<4; i++)
            System.out.print("+------------");
        System.out.print("+-------------");
        System.out.print("+------");
        System.out.println("+");
    }

    public void enteteDocument(){
        cadreDocument();
        System.out.println("|  "+ Y +"TYPE"+ RES +"   | "+ Y +"ID"+ RES +" |               "+ Y +
                "TITRE"+ RES +"                |     "+ Y +"Nom Auteur"+ RES +"     |    "+ Y +"NOM EDITEUR"+ RES +
                "     |   "+ Y +"DATE"+ RES +"   |  "+ Y +"FREQUENCE"+ RES +" |"+ Y +"LOCALISATION"+ RES +
                "| "+ Y +"EXEMPLAIRES"+ RES +"| "+ Y +"DISPONIBLE"+ RES +" | "+ Y +"PRET TOTALE"+ RES +" |  "+ Y +"%"+ RES +"   |");
        cadreDocument();
    }

    public void cadrePret(){
        System.out.print("+---------");
        for (int i=0; i<2; i++)
            System.out.print("+----------------------------------------");
        for (int i=0; i<3; i++)
            System.out.print("+-------------");
        System.out.println("+");
    }

    public void entetePret(){
        cadrePret();
        System.out.println("| "+Y+"TYPE DOC"+RES+"|                 "+Y+"TITRE"+RES+"                  |                "+Y+
                "ADHERENT"+RES+"                |   "+Y+"TYPE AD"+RES+"   |    "+Y+"PRIS LE"+RES+
                "  | "+Y+"A RENDRE LE"+RES+" |");
        cadrePret();
    }

    public void enteteHistorique(){
        System.out.println("+-------------------------+--------+------+---------------+--------------------------------------------------------------------------------+");
        System.out.println("|          "+Y+"DATE"+RES+"           |  "+Y+"CLASS"+RES+" |  "+Y+"ID"+RES+"  |      "+Y+
                "TYPE"+RES+"     |                                    "+Y+"INFO"+RES+"                                        |");
        System.out.println("+-------------------------+--------+------+---------------+--------------------------------------------------------------------------------+");
    }

    public void seeds(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//        AJOUT AUTOMATIQUE DE 5 ADHERENT
        Adherent a1 = new Adherent(95, "slama", "khairi", "Borjine, sousse", "Etudiant", 2, 7);
        Adherent a2 = new Adherent(96, "slama", "khalil", "Borjine, sousse", "Visiteur", 1, 7);
        Adherent a3 = new Adherent(97, "zaatir", "Mounir", "Bohsina, sousse", "Enseignant", 4, 21);
        Adherent a4 = new Adherent(98, "rouis", "houssem", "M'saken, sousse", "Etudiant", 2, 7);
        Adherent a5 = new Adherent(99, "smida", "ghada", "Chott mariem, sousse", "Etudiant", 2, 7);
        listAdherent.add(a1);
        listAdherent.add(a2);
        listAdherent.add(a3);
        listAdherent.add(a4);
        listAdherent.add(a5);
//        AJOUT AUTOMATIQUE DE 5 DOCUMENTS
        Document d1 = new Article(95, "Le corona-virus", "S1/F100", 1, "Information Center", new Date());
        Document d2 = new Article(96, "La nature", "S1/F321", 1, "Nat Geo", new Date());
        Document d3 = new Livre(97, "Harry Potter livre 1", "S1/L112", 2, "J.K Rowling", "Gallimard Jeunesse", new Date());
        Document d4 = new Livre(98, "Le seigneur des anneaux", "S1/L538", 2, "J.R.R Tolkien", "Allen & Unwin", new Date());
        Document d5 = new Magazine(99, "Elle", "S1/M019", 1, 500);
        listDocument.add(d1);
        listDocument.add(d2);
        listDocument.add(d3);
        listDocument.add(d4);
        listDocument.add(d5);
//        AJOUT DE PRETS EN RETARD D'ARTICLE 1 AU 5EME ADHERENT
        PretDocument pretDocument = new PretDocument();
        pretDocument.setAdherent(a5);
        try{
            pretDocument.setDateDuPret(sdf.parse("11/6/2020"));
        }catch (ParseException e){
            System.out.println("rien");
        }
        pretDocument.calculerDateRetour();
        d1.getListPret().add(pretDocument);
        a5.setNbPret(1);
        a5.setNbPretTotale(3);
        nombreDePretTotale++;
        d1.setNbPretTotale(1);
//        ON INITIALISE LE 4EME ADHERENT RETARDATAIRE PAR DEFAUT
        a4.setRetard(true);
        a4.setNbPretTotale(1);
//        ON AJOUT UN PREDOC A L'ADHERENT 1 ET DOC 3
        PretDocument pretDocument2 = new PretDocument();
        pretDocument2.setAdherent(a1);
        try{
            pretDocument2.setDateDuPret(sdf.parse("24/6/2020"));
        }catch (ParseException e){
            System.out.println("rien");
        }
        pretDocument2.calculerDateRetour();
        d3.getListPret().add(pretDocument2);
        a1.setNbPret(1);
        a1.setNbPretTotale(12);
        d3.setNbPretTotale(1);
        nombreDePretTotale++;
    }
}

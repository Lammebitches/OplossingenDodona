
import java.util.ArrayList;



public class HRM implements IHRM {
    private ArrayList<Employee> employees;
    private ArrayList<EmployeeRequest> requests;

    public HRM(ArrayList<Employee> employees) {
        this.employees = employees;
        this.requests = new ArrayList<EmployeeRequest>();
    }
    

    @Override
    public void addVacationRequest(String employeeID, int nrOfDays){
        boolean test = true;
        int count = 0, index =0;
        
        for(int i = 0; i < employees.size() ; i++){
            if(employeeID.equals(this.employees.get(i).getEmployeeID())){
                count ++;
                index = i;
                break;}
        }
        if(count == 1){
            VacationRequest request = new VacationRequest(nrOfDays,this.employees.get(index));
            this.requests.add(request);
        }
        else
            System.out.println("De werknemer bestaat niet!");
    }
    
    @Override
    public void  addReimbursementRequest(String employeeID, double amount, String description){
        int count = 0, index =0;
        
        for(int i = 0; i < employees.size() ; i++){
            if(employeeID.equals(this.employees.get(i).getEmployeeID())){
                count ++;
                index = i;
                break;}
        }
        if(count == 1){
            ReimbursementRequest request = new ReimbursementRequest(amount,description,this.employees.get(index));
            this.requests.add(request);
        }
        else
            System.out.println("De werknemer bestaat niet!");
    }        
//Overschrijf de methode getEmployeeRequests die een String en twee Decision objecten als parameters heeft. De methode haalt de requests van de werknemer
//met de gegeven employeeID op en retourneert. Enkel de requests met de gegeven waarden voor hun beslissingen mogen teruggegeven worden. Indien er geen werknemer 
//bestaat met de gegeven employeeID, wordt de volgende foutboodschap afgeprint in de console: “De werknemer bestaat niet!”. Indien voorgaande foutboodschap weergegeven werd,
//zal de methode null teruggeven.   
    
    
    
    @Override
    public ArrayList<EmployeeRequest>  getEmployeeRequests(String employeeID, Decision getProcessingDecision, Decision mgrDecision){
        ArrayList<EmployeeRequest> lijst = new ArrayList<>();
        boolean test = false;
        for(int i = 0; i < employees.size() ; i++){
            if(employeeID.equals(employees.get(i).getEmployeeID()) && !employeeID.equals("")){
                test = true;}    
        }
        if(test == false){
            System.out.println("De werknemer bestaat niet!");
            return null;}
        
        for(int i =0; i < requests.size(); i++){
        if(employeeID.equals(this.requests.get(i).getRequester().getEmployeeID())&&this.requests.get(i).getPreProcessingDecision().equals(getProcessingDecision) && this.requests.get(i).getManagerDecision().equals(mgrDecision)){
                lijst.add(this.requests.get(i));                
            }
        }
        return lijst;
    }
//Overschrijf de methode preProcessRequests die geen parameters heeft. De methode verwerkt de requests die nog niet goed- of afgekeurd zijn door een manager.
//Voor een VacationRequest zal men kijken of de werknemer nog voldoende dagen beschikbaar heeft en voor een ReimbursementRequest gaat men na of het gevraagde bedrag nog beschikbaar is.
//Indien aan deze voorwaarde voldaan is, zal de PreprocessingDecision van de request gelijk gesteld worden aan Accepted. Indien dit niet het geval is NotAccepted.

    @Override
    public void preProcessRequests(){
            
        for(int i = 0; i < requests.size(); i++){
                if(this.requests.get(i).getManagerDecision().equals(Decision.Undecided)){
                    
                if(requests.get(i) instanceof VacationRequest){
                    VacationRequest vr = (VacationRequest) requests.get(i);
                    if(vr.getNrOfDays() <= vr.getRequester().getNrOfFreeDays())
                        requests.get(i).setPreProcessingDecision(Decision.Accepted);
                    else
                        requests.get(i).setPreProcessingDecision(Decision.NotAccepted);                                                
                }
                else if(requests.get(i) instanceof ReimbursementRequest){
                    ReimbursementRequest rr = (ReimbursementRequest) requests.get(i);
                    if(rr.getAmount() <= rr.getRequester().getBudget())
                        requests.get(i).setPreProcessingDecision(Decision.Accepted);
                    else
                        requests.get(i).setPreProcessingDecision(Decision.NotAccepted);                       
                }
        }}   
    }
//Overschrijf de methode approveRequest die twee Strings als parameters heeft. De methode registreert dat een bepaald request (gegeven de ID van de request) wordt goedgekeurd door de manager 
//(gegeven de ID van deze manager) van de aanvragende werknemer, past ook de gegevens (i.e. budget of nrOfFreeDays) van de werknemer in kwestie aan en roept daarna de preProcessRequests-methode
//weer op (aangezien het aantal vrije dagen of het budget van de employee aangepast is). Indien de request niet bestaat, de manager niet bestaat, de gegeven werknemer niet de manager is van de 
//aanvrager van de request, de request niet de ‘accepted’ status heeft als preprocessing beslissing of de request al een manager beslissing heeft, wordt de volgende foutboodschap afgeprint in de
//console: “De aanvraag kan niet goedgekeurd worden!”. Indien voorgaande foutboodschap weergegeven werd, zal uiteraard geen van de andere stappen van de methode uitgevoerd worden.

    @Override
    public void approveRequest(String requestID, String managerID){
    EmployeeRequest aanvraag;
    boolean valid = true;
    int requestnummer = -1;
    for(int i =0; i < requests.size(); i++){
        if(requests.get(i).getRequestID().equals(requestID) && valid){
            aanvraag = requests.get(i);
            requestnummer = i;
            break;
        }    
    }
    if(requestnummer <0 || !requests.get(requestnummer).getRequester().getManager().getEmployeeID().equals(managerID) ||
            !requests.get(requestnummer).getPreProcessingDecision().equals(Decision.Accepted) || !requests.get(requestnummer).getManagerDecision().equals(Decision.Undecided)){
        System.out.println("De aanvraag kan niet goedgekeurd worden!");        
    }
    else{
        requests.get(requestnummer).setManagerDecision(Decision.Accepted);
        if(requests.get(requestnummer) instanceof VacationRequest){
            VacationRequest vr = (VacationRequest) requests.get(requestnummer);
            vr.getRequester().setNrOfFreeDays(vr.getRequester().getNrOfFreeDays() - vr.getNrOfDays());
            requests.set(requestnummer, vr);
        }
        else if(requests.get(requestnummer) instanceof ReimbursementRequest){
            ReimbursementRequest rr = (ReimbursementRequest) requests.get(requestnummer);
            rr.getRequester().setBudget(rr.getRequester().getBudget() - rr.getAmount());
            requests.set(requestnummer, rr);
        }
        
    this.preProcessRequests();
    }    
    }
 //Overschrijf de methode disapproveRequest die twee Strings als parameters heeft. De methode registreert dat een bepaald request (gegeven de ID van de request) 
 //wordt afgekeurd door de manager (gegeven de ID van deze manager) van de aanvragende werknemer. Indien de request niet bestaat, de manager niet bestaat, de gegeven 
 //werknemer niet de manager is van de aanvrager van de request of de request al een manager beslissing heeft, wordt de volgende foutboodschap afgeprint in de console:
 //“De aanvraag kan niet afgekeurd worden!”. Indien voorgaande foutboodschap weergegeven werd, zal uiteraard geen van de andere stappen van de methode uitgevoerd worden.

    @Override
    public void disapproveRequest(String requestID, String managerID){
    EmployeeRequest aanvraag;
    int requestnummer = -1;
    for(int i =0; i < requests.size(); i++){
        if(requests.get(i).getRequestID().equals(requestID)){
            aanvraag = requests.get(i);
            requestnummer = i;
            break;
        }    
    }
    if(requestnummer <0 || !requests.get(requestnummer).getRequester().getManager().getEmployeeID().equals(managerID) || !requests.get(requestnummer).getManagerDecision().equals(Decision.Undecided)){
        System.out.println("De aanvraag kan niet afgekeurd worden!");        
    }
    else{
        requests.get(requestnummer).setManagerDecision(Decision.NotAccepted); 
    }    
    }
    
//Overschrijf de methode toString die geen parameters heeft. De methode retourneert achtereenvolgens voor elk werknemer de goedgekeurde aanvragen, de geweigerde aanvragen en de aanvragen
//die nog niet werden behandeld door zijn manager. De werknemers worden alfabetisch weergegeven volgens hun employeeID. Hieronder een voorbeeld (het aantal streepjes maakt niet uit).
//Let op: een request waarvan de preprocessing beslissing negatief is maar waarvoor er nog geen manager beslissing is, zal als geweigerd gerekend worden.
//Zelfs al zou de manager deze willen goedkeuren, is dit niet meer mogelijk. @
    @Override
    public  String toString(){
    String antwoord ="";
    ArrayList<Employee> copyE = employees, alfabetisch = null;
    int count = 0;
    boolean flag = true;
    Employee temp;
    //bubble sort
    while(flag){
        flag = false;
        for(int i=0; i< copyE.size()-1; i++){
            if(copyE.get(i).compareTo(copyE.get(i+1))>0){
                temp = copyE.get(i);
                copyE.set(i,copyE.get(i+1));
                copyE.set(i+1,temp);
                flag=true;
            }
        }
    }
    
    for(int k=0; k < employees.size(); k++){
    boolean test = false;
        antwoord += "\n" + copyE.get(k).getEmployeeID()+" "+copyE.get(k).getFirstname()
        + " " + copyE.get(k).getLastname();
        
        if(copyE.get(k).getManager() != null){
            antwoord += " (Manager: " + copyE.get(k).getManager().getEmployeeID()+ ")";
        }
        
        antwoord += "\n" + "------------------------" +"\n" + "Goedgekeurde aanvragen:";
        for(int m=0; m< requests.size(); m++){
            if(requests.get(m).getManagerDecision().equals(Decision.Accepted) && requests.get(m).getRequester().getEmployeeID().equals(copyE.get(k).getEmployeeID())){
            test = true;
            antwoord += "\n" + requests.get(m).toString();  
            }
        }
        if(!test)
            antwoord += "\n" + "Geen requests beschikbaar";
    boolean test1 = false;        
        antwoord += "\n" + "Geweigerde aanvragen:";
        for(int m=0; m< requests.size(); m++){
            if(requests.get(m).getPreProcessingDecision().equals(Decision.NotAccepted) || requests.get(m).getManagerDecision().equals(Decision.NotAccepted)){
                if(requests.get(m).getRequester().getEmployeeID().equals(copyE.get(k).getEmployeeID())){
            test1 = true;
            antwoord += "\n" + requests.get(m).toString();  
            }}
        }
        if(test1 == false)
            antwoord += "\n" + "Geen requests beschikbaar";       
    boolean test2 = false;        
        antwoord += "\n" + "Aanvragen die nog moeten goedgekeurd worden:";
        for(int m=0; m< requests.size(); m++){
            if(!requests.get(m).getPreProcessingDecision().equals(Decision.NotAccepted)&& requests.get(m).getManagerDecision().equals(Decision.Undecided) && requests.get(m).getRequester().getEmployeeID().equals(copyE.get(k).getEmployeeID())){
            test2 = true;
            antwoord += "\n" + requests.get(m).toString();  
            }
        }
        if(test2 == false)
            antwoord += "\n" + "Geen requests beschikbaar" ;          
    }        
    return antwoord;
    }
}
    


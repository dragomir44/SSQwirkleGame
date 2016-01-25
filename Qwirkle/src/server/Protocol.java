package server;

/**
 * Protocol groep 2 (BIT) 2016 
 * Interface Protocol met daarin de gemaakte protocol afspraken
 * @author  Wouter Bolhuis & Cas Putman
 * @version 1.2.1 (13-01-2016)
 * 
 * @changelog
 * - Commands waarmee kan worden opgevraagd hoeveel spelers er in de game zitten.
 * - SERVER_CORE_DONE wordt niet meer gebruikt na het doorgeven van de moves naar andere spelers, ipv daarvan wordt meteen SERVER_CORE_TURN gestuurd.
 * 
 * Vragen of opmerkingen? 
 * Stuur ons een email: 

 */


public interface Protocol {
	
	/* ---------------------------CORE COMMANDS ---------------------------	 */
	
	/* Wordt gezien als de scheiding tussen alle individuele delen van het commando
	 * Na dit teken komt het volgende deel van het commando
	 * bijvoorbeeld de volgende naam of zoiets.
	 */
	String MESSAGESEPERATOR = " ";	
	
	/* De poort die als standaard wordt gebruikt om verbinding te maken met een server
	 */
	String PORT = "1337";
	
	/* Wordt gebruikt door de client direct na verbinding te maken met de server.
	 * Om alle extensies die door de client worden ondersteund door te geven.
	 * De namen van de extensies staan onderaan aan gegeven
	 * Richting: Client -> Server.
	 * 
	 * @param extension = Naam van de ondersteunde extensies
	 * Dit kunnen meerdere extensies zijn, met spaties tussen elke extensie
	 * 
	 * @require extension bevat geen spaties
	 */
	String CLIENT_CORE_EXTENSION = "ex";
	
	/* Wordt gebruikt om vanuit de server aan de client te melden welke extensies gebruikt kunnen worden.
	 * De namen van de extensies staan onderaan aan gegeven
	 * Richting: Server -> Client
	 * 
	 * @param extension = naam van de extensies die de server en de client overeenkomen 
	 * Dit kunnen meerdere extensies zijn, met spaties tussen elke extensie
	 * Dit kunnen er nooit meer zijn dan de client heeft gestuurd.
	 *  
	 * @require extension bevat geen spaties
	 */
	String SERVER_CORE_EXTENSION = "returnEx";
	
	/* Manier om zelf een naam de definieren bij de server. Nodig voor bijvoorbeeld challenge en leaderboard plugins.
	 * Richting: Client -> Server
	 * 
	 * @param name = Naam waarmee men bekend wilt staan bij de server.
	 * 
	 * @require name bevat geen spaties.
	 */
	String CLIENT_CORE_LOGIN = "login";
	
	/* Als de login succesvol is.
	 * Richting: Server -> Client
	 */
	String SERVER_CORE_LOGIN_ACCEPTED = "loginAccepted";
	
	/* Als de login niet succesvol is.
	 * bijvoorbeeld als er al iemand is met die naam.
	 * of als deze naam alleen in kan loggen met secure login.
	 * Richting: Server -> Client 
	 */
	String SERVER_CORE_LOGIN_DENIED = "loginDenied";
	
	/* Het commando om deel te nemen aan een game zonder daarbij eerst zelf een naam te definieren
	 * Richting: Client -> Server
	 */
	String CLIENT_CORE_JOIN = "join";
	
	/* Als het join request van de client gelukt is
	 * Richting: Server -> Client
	 * 
	 * @param name = Naam die de server vanaf nu hanteert voor de client
	 * 
	 * @require name bevat geen spaties
	 */
	String SERVER_CORE_JOIN_ACCEPTED = "joinAccepted";
	
	/* Als het join request van de client niet uitgevoerd kan worden
	 * Richting: Server -> Client 
	 */
	String SERVER_CORE_JOIN_DENIED = "joinDenied";
	
	/* Het verzoek om te weten welke spelers er in de game zitten.
	 * Richting: Client -> Server 
	 */
	String CLIENT_CORE_PLAYERS = "getPlayers";
	
	/* Om de client te laten weten wie er in de game zitten. 
	 * Richting: Server -> Client
	 * 
	 * @param name = naam van een spelers die in de game zit
	 * Dit kunnen meerdere namen zijn, met spaties tussen elke naam
	 * 
	 * @require name bevat geen spaties.
	 */
	String SERVER_CORE_PLAYERS = "sendPlayers";
	
	/* Het verzoek van de client naar de server om te beginnen.
	 * Richting: Client -> Server 
	 */
	String CLIENT_CORE_START = "start";	
	
	/* Het bericht dat de server stuurt als er begonnen wordt. 
	 * Richting: Server -> Client (ALL)
	 * 
	 * @param name = naam van een spelers die meespeelt
	 * Dit kunnen meerdere namen zijn, met spaties tussen elke naam
	 * 
	 * @require name bevat geen spaties.
	 */
	String SERVER_CORE_START = "starting";
	
	/* Het bericht van de server aan een client als respons op het start commando indien de server niet meteen gaat beginnen
	 * Richting: Server -> Client 
	 */
	String SERVER_CORE_START_DENIED = "startDenied";
	
	/* De manier waarop de server duidelijk maakt wie er aan de beurt is.
	 * Dit wordt onder andere gestuurd nadat alle moves door de server zijn doorgegeven.
	 * Richting: Server -> Client (ALL)
	 * 
	 * @param name = naam van de client die aan de beurt is
	 * 
	 * @require name bevat geen spaties
	 */
	String SERVER_CORE_TURN = "turn";
	
	/* Het commando om een move door te geven aan de server.
	 * Nadat alle moves zijn doorgegeven stuurt de client ook het done command.
	 * Richting: Client -> Server
	 * 
	 * @param x = x coordinaat van de plaatsing
	 * @param y = y coordinaat van de plaatsin
	 * @param shape = de gekozen vorm
	 * @param colour = de gekozen kleur
	 * 
	 * @require x = integer
	 * @require y = integer
	 * @require shape = integer
	 * @require colour = integer
	 */
	String CLIENT_CORE_MOVE = "move";
	
	/* Na het move bericht is dit ��n van de mogelijke responses
	 * Richting: Server -> Client
	 */
	String SERVER_CORE_MOVE_ACCEPTED = "moveAccepted";
	
	/* Dit is de andere mogelijke response.
	 * Dit bijvoorbeeld als de tile niet in de inventory van de client zit.
	 * Of als er al een tile op die coordinaten ligt.
	 * Richting: Server -> Client
	 */
	String SERVER_CORE_MOVE_DENIED = "moveDenied";
	
	/* Als de move geaccepteerd is door de server stuurt deze ook meteen dit command naar alle spelers.
	 * Nadat de server alle gemaakt moves door een speler heeft doorgegeven stuurt de server ook het done command.
	 * Richting: Server -> Client (ALL)
	 * 
	 * @param x = x coordinaat van de plaatsing
	 * @param y = y coordinaat van de plaatsing
	 * @param shape = vorm van de tile
	 * @param colour = kleur van de tile
	 * 
	 * @require x = integer
	 * @require y = integer
	 * @require shape = integer
	 * @require colour = integer
	 */
	String SERVER_CORE_MOVE_MADE = "moveMade";
	
	/* Voor de client om aan te geven dat ze klaar zijn met een reeks commando's sturen
	 * Dit wordt gebruikt na:
	 * - het doorgeven van alle moves aan de server
	 * - het doorgeven van alle steentjes die ze willen vervangen.
	 * Richting: Client -> Server
	 */
	String CLIENT_CORE_DONE = "done";
	
	/* Voor de server om aan te geven dat ze klaar is met een reeks commando's sturen
	 * Dit wordt gebruikt na:
	 * - de server klaar is met een client tiles sturen 
	 * Richting: Server -> Client
	 */
	String SERVER_CORE_DONE = "done";
	
	/* Het commando om de scores van alle spelers door te sturen.
	 * Dit commando wordt gebruikt nadat een client klaar is met alle moves maken.
	 * Richting: Server -> Client (ALL)
	 * 
	 * @param name = naam van de speler
	 * @param score = score van de speler
	 * Dit gaat altijd over meerdere spelers, met spaties tussen elk deel
	 * 
	 * @require name bevat geen spaties
	 * @require score = integer
	 */
	String SERVER_CORE_SCORE = "score";
	
	/* Nadat een speler moves heeft gedaan, of tiles heeft geswapt stuurt de server deze clients genoeg tiles om ervoor te zorgen dat de speler weer zes tiles heeft.
	 * Nadat alle tiles zijn doorgegeven gebruikt de server ook het done command.
	 * Richting: Server -> Client
	 * 
	 * @param shape = vorm van de tile
	 * @param colour = kleur van de tile
	 * 
	 * @require shape = integer
	 * @require colour = integer
	 */
	String SERVER_CORE_SEND_TILE = "receiveTile";
	
	/* Als een speler in plaats van een move wilt doen van tiles af wilt
	 * Nadat alle tiles zijn doorgegeven gebruikt de client ook het done command.
	 * Richting: Client -> Server
	 * 
	 * @param shape = vorm van de tile
	 * @param colour = kleur van de tile
	 * 
	 * @require shape = integer
	 * @require colour = integer
	 */
	String CLIENT_CORE_SWAP = "swap";
	
	/* Als de swap van de client met succes is afgerond.
	 * Richting: Server -> Client
	 */
	String SERVER_CORE_SWAP_ACCEPTED = "swapAccepted";
	
	/* Als de swap van de client niet mogelijk is
	 * Bijvoorbeeld als de tile niet in de inventory is of als er geen tiles meer in de pot zitten
	 * Richting: Server -> Client
	 */
	String SERVER_CORE_SWAP_DENIED = "swapDenied";
	
	/* Als de game is afgelopen stuurt de server dit command.
	 * Daarbij stuurt de server ook meteen de score mee zoals bij het score command
	 * Richting: Server -> Client (ALL)
	 * 
	 * @param name = naam van de speler
	 * @param score = score van de speler
 	 * Dit gaat altijd over meerdere spelers, met spaties tussen elk deel
	 * 
	 * @require name bevat geen spaties
	 * @require score = integer
	 */
	String SERVER_CORE_GAME_ENDED = "gameEnded";
	
	/* Als ��n van de spelers om ��n of andere reden verbinding verliest dan kan de server dat aan de clients doorgeven.
	 * Richting: Server -> Client (ALL)
	 * 
	 * @param name = naam van de client die verbinding verloor
	 * 
	 * @require name bevat geen spaties
	 */
	String SERVER_CORE_TIMEOUT_EXCEPTION = "exception timeout";
}
	
	
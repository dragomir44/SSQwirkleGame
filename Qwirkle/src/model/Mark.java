package model;

//VORIG PROJECT

/**
 * Represents a mark in the Connect Four game. There three possible values:
 * Mark.XX, Mark.RR and Mark.YY.
 * 
 * @author Sergey Dragomiretskiy and Amber Altenburg
 * @version $Date: 22-1-2015 $
 */
public enum Mark {
    
    EE, RR, YY;

    /*@
       ensures this == Mark.RR ==> \result == Mark.YY;
       ensures this == Mark.YY ==> \result == Mark.RR;
       ensures this == Mark.EE ==> \result == Mark.EE;
     */
    /**
     * Returns the other mark.
     * 
     * @return the other mark if this mark is not EE
     */
    public Mark other() {
        if (this == RR) {
            return YY;
        } else if (this == YY) {
            return RR;
        } else {
            return EE;
        }
    }
}

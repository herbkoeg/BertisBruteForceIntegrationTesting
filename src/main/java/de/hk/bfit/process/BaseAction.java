/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hk.bfit.process;

import java.util.List;

/**
 *
 * @author palmherby
 */

public class BaseAction {

    protected String description;
    protected List<String> sqlCommmand;
    protected Boolean rollBackOnError;
    
}

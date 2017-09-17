/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package astar.model;

import astar.plugin.IModel;
import static astar.util.Helper.tracksWall;
import astar.util.Node;

/**
 *
 * @author Tom
 */
public class Stealthy implements IModel{

    protected char[][] tileMap; 
    @Override
    public void init(char[][] tileMap) {
       this.tileMap = tileMap;
    }

    @Override
    public double shape(double heuristic, Node curNode, Node adjNode) {
        
        if( tracksWall( tileMap , adjNode )
            ||  tracksWall( tileMap , curNode ) ) {
            
            heuristic-=heuristic*0.75;
        }  
       
        return heuristic;
    }

    @Override
    public void complete(Node curNode) {
        
    }
    
}

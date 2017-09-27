/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package astar.model;

import astar.plugin.IModel;
import static astar.util.Helper.tracksWall;
import static astar.util.Helper.isObstacle;
import astar.util.Node;

/**
 *
 * @author Tom
 */
public class Pretty implements IModel {
    
    protected char[][] tileMap = null;

    @Override
    public void init(char[][] tileMap) { 
        
        this.tileMap = tileMap;    // Store world tileMap in class memeber variable.     
    }

    @Override
    public double shape(double heuristic, Node curNode, Node adjNode) {
        
        if(curNode.getParent()==null){
            
            return heuristic;
        }
            
        Node adjacentNode = new Node(adjNode);
        Node currentNode = new Node(curNode);
        Node cur_parentNode = new Node(curNode.getParent());
         
        // Zag condition: P' Zags when A(i-2).col - A(i-1).col != A(i-1).col - A(i).col 
        //                          or A(i-2).row - A(i-1).row != A(i-1).col - Ai(i).row
        
        boolean isZagging = ( cur_parentNode.getCol() - currentNode.getCol() ) 
                             != ( currentNode.getCol() - adjacentNode.getCol())
                                || ( cur_parentNode.getRow() - currentNode.getRow() ) 
                                    != ( currentNode.getRow() - adjacentNode.getRow() );
        
        boolean isTracking = (tracksWall( tileMap, adjNode ) || tracksWall( tileMap, curNode ) );
        
        //Experimentally determined value of tau(Inertia streak) is 8
        
        if(adjNode.getInertia()>=8){
            
            return heuristic;                                                   // if inertia reaches 8, it is reset
        }
        else if( !isZagging && !isTracking ){
            adjNode.setInertia(curNode.getInertia()+1);                         // if the agent does not zag, inertia is incremented by 1. 
            return heuristic;
        }
        else if( isZagging && !isTracking ){
            return heuristic+2;                                                 // penalty is also imposed on the agent whenever it zags.  
        }
        else if( !isZagging && isTracking){
            adjNode.setInertia(curNode.getInertia()+1);
            return heuristic+10;                                                // penalty is imposed on the agent whenever it tracks a wall/obstacle
        }
        else{
            return heuristic+13;
        }
    }

    @Override
    public void complete(Node curNode) {
        
        
       while(curNode.getParent().getParent().getParent()!=null)
        {
            fixBridges(curNode);
            fixCaterCorners(curNode);
            curNode=curNode.getParent();
        }
       
        
        
    }
    
    public void fixBridges(Node curNode){
        
        Node parent = curNode.getParent();
        Node grandParent = curNode.getParent().getParent();
        Node greatGrandParent = curNode.getParent().getParent().getParent();
        if( curNode.getRow() == greatGrandParent.getRow() ){
            
            parent.setRow(curNode.getRow());
            grandParent.setRow(curNode.getRow());
        }
        else if( curNode.getCol() == greatGrandParent.getCol()){
             parent.setCol(curNode.getCol());
             grandParent.setCol(curNode.getCol());
             
        }
    }
    
    public void fixCaterCorners(Node curNode){
        
        System.out.println("0");
        Node parent = curNode.getParent();
        Node child = null;
        if(curNode.getChild()!=null){
            child=curNode.getChild();
        }
            
        Node newNode = null;
        if(isObstacle(tileMap,curNode.getCol()+1,curNode.getRow()-1)){
            if(parent.getRow()<child.getRow()){
                System.out.println("1");
                newNode = new Node( parent.getCol() ,parent.getRow()+1 ,parent );
                parent.setChild(newNode);
                newNode.setParent(parent);
                newNode.setChild(curNode);
                curNode.setParent(newNode);
                curNode.setRow(curNode.getRow()+1);
            }
            else{
                newNode = new Node( child.getCol() ,child.getRow()+1, child );
                child.setParent(newNode);
                newNode.setChild(child);
                newNode.setParent(curNode);
                curNode.setChild(newNode);
                curNode.setRow(curNode.getRow()+1);
            }
        }
        
        if(isObstacle(tileMap,curNode.getCol()-1,curNode.getRow()+1)){
            System.out.println("2");
            if(parent.getRow()<child.getRow()){
                newNode = new Node( parent.getCol()+1, parent.getRow(), parent );
                parent.setChild(newNode);
                newNode.setParent(parent);
                newNode.setChild(curNode);
                curNode.setParent(newNode);
                curNode.setCol(curNode.getCol()+1);
            }
            else{
                
                newNode = new Node( child.getCol()+1, child.getRow(), child);
                child.setParent(newNode);
                newNode.setChild(child);
                newNode.setParent(curNode);
                curNode.setChild(newNode);
                curNode.setCol(curNode.getCol()+1);
            }
            
        }
        
        if(isObstacle(tileMap, curNode.getCol()+1,curNode.getRow()+1)){
             System.out.println("3");
            if(parent.getRow()<child.getRow()){
                
                newNode = new Node( parent.getCol()-1, parent.getRow(), parent );
                parent.setChild(newNode);
                newNode.setParent(parent);
                newNode.setChild(curNode);
                curNode.setParent(newNode);
                curNode.setCol(curNode.getCol()-1);
            }
            else{
                
                newNode = new Node( child.getCol()-1, child.getRow(), child);
                child.setParent(newNode);
                newNode.setChild(child);
                newNode.setParent(curNode);
                curNode.setChild(newNode);
                curNode.setCol(curNode.getCol()-1);
                
            }
        }
        
        if(isObstacle(tileMap,curNode.getCol()-1,curNode.getRow()-1)){
            
            if(parent.getCol()<child.getCol()){
                 System.out.println("4");
                newNode = new Node( parent.getCol()+1, parent.getRow(), parent );
                parent.setChild(newNode);
                newNode.setParent(parent);
                newNode.setChild(curNode);
                curNode.setParent(newNode);
                curNode.setCol(curNode.getCol()+1);
            }
            else{
                 System.out.println("5");
                newNode = new Node( parent.getCol(), parent.getRow()+1, parent );
                parent.setChild(newNode);
                newNode.setParent(parent);
                newNode.setChild(curNode);
                curNode.setParent(newNode);
                curNode.setRow(curNode.getRow()+1);
            }
        }
               
    }
            
            
        
}
    


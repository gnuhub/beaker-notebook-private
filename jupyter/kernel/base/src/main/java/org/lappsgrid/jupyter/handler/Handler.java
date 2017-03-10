package org.lappsgrid.jupyter.handler;

public interface Handler<T>{
  
   void handle(T message) ;
   
}
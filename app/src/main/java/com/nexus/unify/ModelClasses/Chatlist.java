package com.nexus.unify.ModelClasses;

import java.io.Serializable;

public class Chatlist implements Serializable {
   public String id;

   public Chatlist(String id) {
       this.id = id;
   }

   public Chatlist() {
   }

   public String getId() {
       return id;
   }

   public void setId(String id) {
       this.id = id;
   }
}

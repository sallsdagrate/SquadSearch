package com.drp52;

import org.junit.Test;

import static org.junit.Assert.*;

import android.content.Context;

import com.drp52.data.database.DatabaseAdapter;
import com.drp52.data.database.FirebaseAdaptor;
import com.drp52.data.database.Player;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest{
    @Test
    public void addition_isCorrect() {
        assertEquals(4 , 2 + 2);
    }

//    @Test
//    public void getAuthWorks() {
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        assertTrue(true);
//    }
//    @Test
//    public void creationAndDeletionOfPlayerOnDatabase() {
//        DatabaseAdapter db = new FirebaseAdaptor();
//        CollectionReference playerRef = FirebaseFirestore.getInstance().collection("players");//Make sure document does not exist
//        Task<DocumentSnapshot> task = playerRef.document("test").get();
//        assertTrue(task.isSuccessful());
//        DocumentSnapshot document = task.getResult();
//        assertFalse(document.exists());
//
//        //Create player
//        boolean status = db.createPlayer(new Player("test",
//                "firstname",
//                "surname",
//                Collections.emptyList()));
//        assertTrue(status);
//
//        //Make sure document does exist
//        task = playerRef.document("test").get();
//        assertTrue(task.isSuccessful());
//        document = task.getResult();
//        assertTrue(document.exists());
//
//        //delete player
//        status = db.deletePlayer("test");
//        assertTrue(status);
//
//        //Make sure document does not exist
//        task = playerRef.document("test").get();
//        assertTrue(task.isSuccessful());
//        document = task.getResult();
//        assertFalse(document.exists());
//    }
}
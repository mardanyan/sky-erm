package com.sky.erm.util;

import com.sky.erm.database.record.UserRecord;

import java.lang.reflect.Field;

/**
 * User Record Patcher
 */
public class UserRecordPatcher {

    public void patch(UserRecord existingIntern, UserRecord incompleteIntern) throws IllegalAccessException {

        //GET THE COMPILED VERSION OF THE CLASS
        Class<?> userRecordClass= UserRecord.class;
        Field[] internFields=userRecordClass.getDeclaredFields();
        for(Field field : internFields){
            //CANT ACCESS IF THE FIELD IS PRIVATE
            field.setAccessible(true);

            //CHECK IF THE VALUE OF THE FIELD IS NOT NULL, IF NOT UPDATE EXISTING INTERN
            Object value=field.get(incompleteIntern);
            if(value!=null){
                field.set(existingIntern,value);
            }
            //MAKE THE FIELD PRIVATE AGAIN
            field.setAccessible(false);
        }
    }

}

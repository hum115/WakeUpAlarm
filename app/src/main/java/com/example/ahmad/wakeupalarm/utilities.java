package com.example.ahmad.wakeupalarm;

/**
 * Created by Ahmad on 3/25/2016.
 */
public class utilities {
    // The Array that will hold the Herat Rate
    public class HRarray{
        int[] hrArray;
        int averageHR;
        HRarray(int size){
            hrArray = new int[size];
            for (int i=0;i<size;i++){
                hrArray[i]=0;
            }
            setAverage();
        }
        void setAverage(){
            int tempSum=0;
            for (int i=0;i<hrArray.length;i++){
                tempSum = tempSum + hrArray[i];
            }
            tempSum=tempSum/hrArray.length;
        }
        void addValue(int a){
            if(hrArray[hrArray.length-1]!=0){
                //need to copy to new array
                int[] temp;
                temp = new int[hrArray.length];
                for(int i=1;i<hrArray.length;i++){
                    temp[i-1]=hrArray[i];
                }
                temp[hrArray.length]=0;
                hrArray=temp;
            }
            for(int i=0; hrArray.length > i;i++)
            {
                if(hrArray[i]==0){
                    hrArray[i]=a;
                    break;
                }


            }
            setAverage();
        }
        int getAverage(){
            setAverage();
            return averageHR;
        }
    }

    public boolean isHRbigger(HRarray initial,HRarray NewHR){
        return (initial.getAverage()<=NewHR.getAverage()*1.15);
    }





}

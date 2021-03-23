package com.example.custodian;

public class BackgroundGenerator {

    // Get randomised image
    public String splash() {
        String returnValue = "";
        Integer numberGenerator = (int) (Math.random() * (3 - 0)) + 0;
        System.out.println("Background generated: " + numberGenerator);
        switch (numberGenerator) {
            case 0:
                returnValue = "https://infs3605-project.weebly.com/uploads/1/2/4/7/124751842/background-uluru_orig.png";
                break;
            case 1:
                returnValue = "https://infs3605-project.weebly.com/uploads/1/2/4/7/124751842/background-wilpena-pound_orig.png";
                break;
            case 2:
                returnValue = "https://infs3605-project.weebly.com/uploads/1/2/4/7/124751842/background-kata-tjuta_orig.png";
                break;
        }
        return returnValue;
    }

    // Get randomised image
    public String login() {
        String returnValue = "";
        Integer numberGenerator = (int) (Math.random() * (3 - 0)) + 0;
        System.out.println("Background generated: " + numberGenerator);
        switch (numberGenerator) {
            case 0:
                returnValue = "https://infs3605-project.weebly.com/uploads/1/2/4/7/124751842/background-indigenous-artwork_orig.png";
                break;
            case 1:
                returnValue = "https://infs3605-project.weebly.com/uploads/1/2/4/7/124751842/background-indigenous-art_orig.png";
                break;
            case 2:
                returnValue = "https://infs3605-project.weebly.com/uploads/1/2/4/7/124751842/background-dreamtime-art_orig.png";
                break;
        }
        return returnValue;
    }

    // Get randomised image
    public String welcome() {
        String returnValue = "";
        Integer numberGenerator = (int) (Math.random() * (3 - 0)) + 0;
        System.out.println("Background generated: " + numberGenerator);
        switch (numberGenerator) {
            case 0:
                returnValue = "https://infs3605-project.weebly.com/uploads/1/2/4/7/124751842/background-people-one_orig.png";
                break;
            case 1:
                returnValue = "https://infs3605-project.weebly.com/uploads/1/2/4/7/124751842/background-people-two_orig.png";
                break;
            case 2:
                returnValue = "https://infs3605-project.weebly.com/uploads/1/2/4/7/124751842/background-people-three_orig.png";
                break;
        }
        return returnValue;
    }
}

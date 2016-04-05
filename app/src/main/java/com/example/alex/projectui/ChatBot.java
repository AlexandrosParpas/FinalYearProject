package com.example.alex.projectui;

import java.util.ArrayList;
import java.util.Random;

public class ChatBot {

    private final int maxResp = 3;
    private Random randomGenerator;
    ArrayList<String> result;
    ArrayList<String> visualCue;
    String visualMatch = "";
    String match = "";

    private static final String[][] KnowledgeBase = {
            {"Hello",
                    "Hey, how are you?|hello",
                    "Hi, I’m glad to see you.|hello",
                    "Hi.|hello"
            },
            {"How are you",
                    "I’m doing very good thanks.|thumbup",
                    "I’m doing well and you?|thumbup",
                    "Why do you want to know how am I doing?|why"
            },
            {"Who are you",
                    "I’m your assistant|teacher",
                    "I’m your teacher|teacher",
                    "Why are you asking?|why"
            },
            {"Are you intelligent",
                    "Yes of course.|yes",
                    "Yes, I am intelligent|yes",
                    "Why are you asking?|why"
            },
            {"Are you real",
                    "Yes, I am real.|yes",
                    "Why are you asking?|why",
                    "No, I'm not real.|no"
            },
            {"Are you a human",
                    "No, I'm a program.|no",
                    "Why are you asking?|why",
                    "No, I'm not a human.|no"
            },
            {"Are you sure",
                    "Yes, I'm sure|yes",
                    "Are you not convinced?|why",
                    "Of course I'm sure.|yes"
            },
            {"Bye",
                    "See you later.|bye",
                    "Bye.|bye",
                    "See you.|bye"
            },
            {"Goodbye",
                    "Goodbye.|bye",
                    "See you later.|bye",
                    "Bye.|bye"
            },
            {"Goodnight",
                    "Goodnight.",
                    "See you later. ",
                    "Bye."
            },
            {"Good morning",
                    "Good morning.",
                    "Hello. ",
                    "How are you?"
            },
            {"This is cool",
                    "Thank you.|happy",
                    "I agree.|happy",
                    "Why do you think it's cool?|why"
            },
            {"How is your day",
                    "My day has been good, thanks.|thumbup",
                    "My day has been boring so far.|thumbdown",
                    "My day has been bad.|thumbdown"
            },
            {"Have a nice day",
                    "Thank you.|happy",
                    "Have a nice day.|happy",
                    "You too.|happy"
            },
            {"Do you speak English",
                    "Yes i speak English.|yes",
                    "No I don't speak English.|no",
                    "Why are you asking?|why"
            },
            {"Thank you",
                    "You're welcome.|happy",
                    "No problem.|thumb up",
                    "Thank you.|happy"
            }
    };

    private static final String[][] GreetingBase = {
            {"What is your name",
                    "My name is Kato.",
                    "Why are you asking?|why",
                    "My name is Kato."
            },
            {"How old are you",
                    "I am three months old.|three",
                    "I am six months old.|six",
                    "I am nine months old.|nine"
            },
            {"Nice to meet you",
                    "Nice to meet you.|meet",
                    "Nice to meet you too.|meet",
                    "Nice to meet you too.|meet"
            },
            {"Have you ever been to Cyprus",
                    "Yes I have been to Cyprus.|yes",
                    "No I haven't been to Cyprus.|no",
                    "Why are you asking?|why"
            },
            {"What is your favourite movie",
                    "My favorite movie is The Shining.|movie",
                    "My favorite movie is Godfather.|movie",
                    "I don't have a favourite movie.|confused"
            },
            {"What is your favourite TV show",
                    "My favorite TV show is Prison Break.|tv",
                    "My favorite TV show is Lost.|tv",
                    "I don't have a favourite TV show.|confused"
            },
            {"What is your favourite colour",
                    "My favourite colour is blue.|blue",
                    "My favourite colour is red.|red",
                    "My favourite colour is green.|green"
            },
            {"Can I have your number",
                    "Yes, you can have my number.|yes",
                    "No, you can't have my number.|no",
                    "Why are you asking?|why"
            },
            {"What is your number",
                    "One, two, three, four, five, six, seven, eight, nine, ten",
                    "Ten, twenty, thirty, fourty, fifty.",
                    "Sixty, seventy, eighty, ninety, zero, zero."
            },
            {"What sports do you like",
                    "I like tennis, football and basketball.|tennis",
                    "I like hockey and volley.|volley",
                    "I don't like sports.|thumbdown"
            },
            {"Do you have any pets",
                    "I have two dogs and one cat.|dogcat",
                    "I have three cats.|cats",
                    "I don't have any pets.|no"
            },
            {"What music do you like",
                    "I like heavy metal music.|music",
                    "I like pop and jazz music.|music",
                    "I don't listen to music.|confused"
            },
            {"Do you have any brothers or sisters",
                    "I have one brother and two sisters.|sisters",
                    "I have three brothers.|brothers",
                    "I don't have any brothers or sisters.|no"
            },
            {"What do you think of England",
                    "I really like England.|thumbup",
                    "I don't like England.|thumbdown",
                    "Why are you asking?|why"
            },
            {"Where are you from",
                    "I am from Cyprus.|cyprus",
                    "I am from Greece.|greece",
                    "Why are you asking where I am from?|why"
            },
            {"What is your job",
                    "I'm a teacher|teacher",
                    "I'm a student.|student",
                    "I don't have a job|no"
            },
            {"Do you have Facebook",
                    "Yes, I have Facebook.|yes",
                    "No,I don't have Facebook.|no",
                    "Why are you asking if I have Facebook?|why"
            },
            {"What languages do you speak",
                    "I speak English, Greek and French.|greekfrench",
                    "I speak English, German and Italian.|germanitalian",
                    "I speak English, Russian and Swedish.|russianswedish"
            }
    };
    private static final String[][] TravelBase = {
            {"Excuse me",
                    "How can I help you?|help",
                    "Hello, how can I help you?|hello",
                    "I'm in a rush.|irritated"
            },
            {"My name is Alex",
                    "Nice to meet you.|meet",
                    "My name is Kato. Nice to meet you.|meet",
                    "That's a nice name.|happy"
            },
            {"Do you speak English",
                    "Yes I do.|yes",
                    "Of course I speak English.|yes",
                    "Why are you asking me if I speak English?|why"
            },
            {"How can I go to to the train station",
                    "You turn left from here.|left",
                    "You turn right from here.|right",
                    "You go forward for one hundred meters.|forward"
            },
            {"How much does this cost",
                    "It costs ten euros.|teneuro",
                    "It costs five euros and fifty cents|fivefifty",
                    "It costs twenty pounds|twentypound"
            },
            {"Where is the nearest hospital",
                    "It's on your left|left",
                    "It's on your right|right",
                    "Go forward|forward"
            },
            {"Can you speak more slowly please",
                    "Yes, sorry.|yes",
                    "What would you like me to repeat?|why",
                    "No I can't|no"
            },
            {"Can I have a receipt",
                    "Yes of course.|yes",
                    "Yes, you can have a receipt.|yes",
                    "Why do you want a receipt?|why"
            },
            {"Can I get a refund please",
                    "Yes, of course.|yes",
                    "I'm sorry, we don't give refunds.|neutral",
                    "Why do you want a refund?|why"
            },
            {"Call the ambulance",
                    "Calling now.",//TODO
                    "What's the emergency?|why",
                    "What happened?|why"
            },
            {"Help",
                    "How can I help you?|why",
                    "What happened?|neutral",
                    "What's wrong?|neutral"
            },
            {"Where can I get help",
                    "Call nine one one.|ambulance",//TODO
                    "Call the ambulance.|ambulance",//TODO
                    "Maybe I can help?|why"
            },
            {"What time do you close",
                    "We close at three.|three",
                    "We close at six.|six",
                    "We close at nine.|nine"
            },
            {"Where is the closest museum",
                    "Turn left from here.|left",
                    "Turn right from here.|right",
                    "Go forward and you will see it.|forward"
            },
            {"Can I get a ticket please",
                    "Yes, you can get a ticket.|yes",
                    "Tickets are not available.|no",
                    "Why do you want tickets?|why"
            },
            {"How much does this cost",
                    "It costs ten euros.|ten",
                    "It costs five euros and fifty cents.|fivefifty",
                    "It costs twenty pounds.|twentypound"
            },
            {"What is this",
                    "This is a park.|park",
                    "This is a museum.|museum",
                    "This is an airport.|airport"
            },
            {"How far away is my destination",
                    "One hundrer meters.|onehundredm",
                    "Two hundrer meters.|twohundredm",
                    "Three kilometers.|three"
            },
            {"Where can I find a taxi",
                    "Turn left from here.|left",
                    "Turn right from here.|right",
                    "Go forward and you will find it.|forward"
            },
            {"Where can I find a train",
                    "Turn left from here.|left",
                    "Turn right from here.|right",
                    "Go forward and you will find it.|forward"
            },
            {"Where is the nearest bathroom",
                    "Turn left from here.|left",
                    "Turn right from here.|right",
                    "Go forward and you will find it.|forward"
            },
            {"Where can I get something to eat",
                    "There is a McDonalds on your right.|right",
                    "There is a Burger King on your left.|left",
                    "Go forward for one hundred meters, and you'll find KFC.|forward"
            },
            {"I am lost",
                    "Do you want directions?|why",
                    "Where do you want to go?|why",
                    "Maybe I can help?|why"
            },
            {"Do you take credit cards",
                    "Yes, we take credit cards.|credit",
                    "No, we don't take credit cards.|credit",
                    "We take cash only.|cash"
            },
            {"I do not understand",
                    "I'm sorry to hear that.|confused",
                    "Ask me again.|repeat",
                    "What don't you understand?|why"
            }
    };

    public ChatBot() {
        randomGenerator = new Random();
        result = new ArrayList<>();
        visualCue = new ArrayList<>();
    }

    public static void main(String[] args) {
        ChatBot chatBot;
        chatBot = new ChatBot();
        chatBot.getGreetingsInputs();
        System.out.println();
        chatBot.getTravelInputs();
        System.out.println();

        for (int i = 0; i < KnowledgeBase.length; ++i) {
            for (int j = 0; j < KnowledgeBase[i].length; j++) {
                System.out.print("[" + KnowledgeBase[i][j] + "] ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
        chatBot.findMatch(chatBot.preProcessInput("Hello!!!"), "Greetings");
        chatBot.selectMatch();
    }

    protected String preProcessInput(String input) {
        //Convert string to all lowercase characters, remove any unnecessary spaces and punctuation
        String processedInput = input.toLowerCase().replaceAll("\\s+", " ").replaceAll("[^a-zA-Z ]", "");
        System.out.println(processedInput);
        return processedInput;
    }

    protected String[] splitInput(String input) {
        String[] splitInput = input.split(" ");
        for (String s : splitInput) {
            System.out.println("[" + s + "]");
        }
        return splitInput;
    }

    protected void findMatch(String str, String scen) {
//        System.out.println("Length of KnowledgeBase is: " + KnowledgeBase.length);
        for (int i = 0; i < KnowledgeBase.length; ++i) {
            if (KnowledgeBase[i][0].equalsIgnoreCase(str)) {
//                System.out.println("Found a match");
                for (int j = 1; j <= maxResp; j++) {
                    //The output is split on the | symbol. The second element after the split is the visual cue match
                    String[] splitOutput = KnowledgeBase[i][j].split("\\|");
                    //Add the raw output to the result array
                    result.add(splitOutput[0]);
                    if (splitOutput.length > 1) { //If there's a visual cue match, add it to the visalCue array.
                        visualCue.add(splitOutput[1]);
                    }
                }
                break;
            }
        }
        if (result.isEmpty()) { //If the result hasn't been found in the knowledge base
            switch (scen) {
                case "Greetings":
                    for (int i = 0; i < GreetingBase.length; ++i) {
                        if (GreetingBase[i][0].equalsIgnoreCase(str)) {
                            for (int j = 1; j <= maxResp; j++) {
                                //The output is split on the | symbol. The second element after the split is the visual cue match
                                String[] splitOutput = GreetingBase[i][j].split("\\|");
                                //Add the raw output to the result array
                                result.add(splitOutput[0]);
                                if (splitOutput.length > 1) { //If there's a visual cue match, add it to the visalCue array.
                                    visualCue.add(splitOutput[1]);
                                }
                            }
                        }
                    }
                    break;
                case "Travel":
                    for (int i = 0; i < TravelBase.length; ++i) {
                        System.out.println("TravelBase[i][0] is: " + TravelBase[i][0]);
                        System.out.println("str is: " + str);
                        if (TravelBase[i][0].equalsIgnoreCase(str)) {
                            for (int j = 1; j <= maxResp; j++) {
                                //The output is split on the | symbol. The second element after the split is the visual cue match
                                String[] splitOutput = TravelBase[i][j].split("\\|");
                                //Add the raw output to the result array
                                result.add(splitOutput[0]);
                                if (splitOutput.length > 1) { //If there's a visual cue match, add it to the visalCue array.
                                    visualCue.add(splitOutput[1]);
                                }
                            }
                        }
                    }
                    break;
            }
        }
//        for (String s : result) {
//            System.out.print("[" + s + "] ");
//        }
//        System.out.println("");
//        for (String s : visualCue) {
//            System.out.print("[[" + s + "]] ");
//        }
    }

    protected void selectMatch() {
        int index = randomGenerator.nextInt(maxResp);
        match = result.get(index);
        setVisualMatch(index);
//        System.out.println("");
//        System.out.println(match);
//        System.out.println(visualMatch);
    }

    private void setVisualMatch(int index) {
        if (!visualCue.isEmpty()) {
            visualMatch = visualCue.get(index);
        }
    }

    public String getVisualMatch() {
        return visualMatch;
    }

    public String getMatch() {
        return match;
    }

    protected void clearResults() {
        result.clear();
        visualCue.clear();
    }

    public String[] getGreetingsInputs() {
        int counter;
        String[] greetingsInputs = new String[KnowledgeBase.length + GreetingBase.length];
        for (counter = 0; counter < KnowledgeBase.length; counter++) {
            greetingsInputs[counter] = KnowledgeBase[counter][0];
        }
        for (int j = 0; j < GreetingBase.length; j++) {
            greetingsInputs[counter] = GreetingBase[j][0];
            counter++;
        }
//        for (int k = 0; k < greetingsInputs.length; k++) {
//            System.out.println("Base Entry " + k + ": " + greetingsInputs[k]);
//        }
        return greetingsInputs;
    }

    public String[] getTravelInputs() {
        int counter;
        String[] travelInputs = new String[KnowledgeBase.length + TravelBase.length];
        for (counter = 0; counter < KnowledgeBase.length; counter++) {
            travelInputs[counter] = KnowledgeBase[counter][0];
        }
        for (int j = 0; j < TravelBase.length; j++) {
            travelInputs[counter] = TravelBase[j][0];
            counter++;
        }
//        for (int k = 0; k < travelInputs.length; k++) {
//            System.out.println("Base Entry " + k + ": " + travelInputs[k]);
//        }
        return travelInputs;
    }
}


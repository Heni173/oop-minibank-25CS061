import java.util.*;

enum Move
{
    ROCK,
    PAPER,
    SCISSORS,
    LIZARD,
    SPOCK
}

class RPSLS
{
    public static int winner(Move a, Move b)
    {
        if(a == b)
            return 0;

        switch(a)
        {
            case ROCK:
                if(b == Move.SCISSORS || b == Move.LIZARD)
                    return 1;
                else
                    return -1;

            case PAPER:
                if(b == Move.ROCK || b == Move.SPOCK)
                    return 1;
                else
                    return -1;

            case SCISSORS:
                if(b == Move.PAPER || b == Move.LIZARD)
                    return 1;
                else
                    return -1;

            case LIZARD:
                if(b == Move.PAPER || b == Move.SPOCK)
                    return 1;
                else
                    return -1;

            case SPOCK:
                if(b == Move.ROCK || b == Move.SCISSORS)
                    return 1;
                else
                    return -1;
        }
        return 0;
    }

    public static String message(Move a, Move b)
    {
        if((a==Move.SCISSORS && b==Move.PAPER)||(b==Move.SCISSORS && a==Move.PAPER))
            return "Scissors cut Paper";

        if((a==Move.PAPER && b==Move.ROCK)||(b==Move.PAPER && a==Move.ROCK))
            return "Paper covers Rock";

        if((a==Move.ROCK && b==Move.LIZARD)||(b==Move.ROCK && a==Move.LIZARD))
            return "Rock crushes Lizard";

        if((a==Move.LIZARD && b==Move.SPOCK)||(b==Move.LIZARD && a==Move.SPOCK))
            return "Lizard poisons Spock";

        if((a==Move.SPOCK && b==Move.SCISSORS)||(b==Move.SPOCK && a==Move.SCISSORS))
            return "Spock smashes Scissors";

        if((a==Move.SCISSORS && b==Move.LIZARD)||(b==Move.SCISSORS && a==Move.LIZARD))
            return "Scissors decapitate Lizard";

        if((a==Move.LIZARD && b==Move.PAPER)||(b==Move.LIZARD && a==Move.PAPER))
            return "Lizard eats Paper";

        if((a==Move.PAPER && b==Move.SPOCK)||(b==Move.PAPER && a==Move.SPOCK))
            return "Paper disproves Spock";

        if((a==Move.SPOCK && b==Move.ROCK)||(b==Move.SPOCK && a==Move.ROCK))
            return "Spock vaporizes Rock";

        if((a==Move.ROCK && b==Move.SCISSORS)||(b==Move.ROCK && a==Move.SCISSORS))
            return "Rock crushes Scissors";

        return "";
    }

    public static void main(String args[])
    {
        Scanner sc = new Scanner(System.in);
        Random r = new Random();

        int playerScore = 0;
        int computerScore = 0;

        Move[] moves = Move.values();

        for(int i=1;i<=5;i++)
        {
            System.out.println("\nRound " + i);
            System.out.print("Enter Move (ROCK/PAPER/SCISSORS/LIZARD/SPOCK): ");

            Move player = Move.valueOf(sc.next().toUpperCase());
            Move computer = moves[r.nextInt(5)];

            System.out.println("Player : " + player);
            System.out.println("Computer : " + computer);

            int result = winner(player, computer);

            if(result == 0)
            {
                System.out.println("Round Draw");
            }
            else
            {
                System.out.println(message(player, computer));

                if(result == 1)
                {
                    System.out.println("You Win this Round");
                    playerScore++;
                }
                else
                {
                    System.out.println("Computer Wins this Round");
                    computerScore++;
                }
            }
        }

        System.out.println("\nFinal Score");
        System.out.println("You = " + playerScore);
        System.out.println("Computer = " + computerScore);

        if(playerScore > computerScore)
            System.out.println("You Win " + playerScore + "-" + computerScore);
        else if(computerScore > playerScore)
            System.out.println("Computer Wins " + computerScore + "-" + playerScore);
        else
            System.out.println("Match Draw " + playerScore + "-" + computerScore);

        sc.close();
    }
}
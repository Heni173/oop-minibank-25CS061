
import java.util.*;

// Enum for all available moves
enum Move
{
    ROCK, PAPER, SCISSORS, LIZARD, SPOCK
}

class RPSLS
{
    // Method to find the winner using switch expression
    public static int winner(Move a, Move b)
    {
        if(a == b)
            return 0;

        return switch(a)
        {
            case ROCK -> (b == Move.SCISSORS || b == Move.LIZARD) ? 1 : -1;
            case PAPER -> (b == Move.ROCK || b == Move.SPOCK) ? 1 : -1;
            case SCISSORS -> (b == Move.PAPER || b == Move.LIZARD) ? 1 : -1;
            case LIZARD -> (b == Move.PAPER || b == Move.SPOCK) ? 1 : -1;
            case SPOCK -> (b == Move.ROCK || b == Move.SCISSORS) ? 1 : -1;
        };
    }

    // Method to display the winning rule
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

        // Getting all enum values
        Move[] moves = Move.values();

        // Playing 5 rounds
        for(int i = 1; i <= 5; i++)
        {
            System.out.println("\nRound " + i);
            System.out.print("Enter Move (ROCK/PAPER/SCISSORS/LIZARD/SPOCK): ");

            Move player = Move.valueOf(sc.next().toUpperCase());

            // Selecting a random computer move
            Move computer = moves[r.nextInt(5)];

            System.out.println("Player : " + player);
            System.out.println("Computer : " + computer);

            // Finding the round winner
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

        // Displaying final score
        System.out.println("\nFinal Score");
        System.out.println("You = " + playerScore);
        System.out.println("Computer = " + computerScore);

        // Displaying overall winner
        if(playerScore > computerScore)
            System.out.println("You Win " + playerScore + "-" + computerScore);
        else if(computerScore > playerScore)
            System.out.println("Computer Wins " + computerScore + "-" + playerScore);
        else
            System.out.println("Match Draw " + playerScore + "-" + computerScore);

        sc.close();
    }
}

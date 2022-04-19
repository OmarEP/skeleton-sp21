package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ...
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                Repository.initCommand();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                Repository.addCommand(args[1]);
                break;
            // TODO: FILL THE REST IN
            case "commit":
                if (args.length == 1 || args[1].isBlank()) {
                    System.out.println("Please enter a commit message.");
                    System.exit(0);
                }
                Repository.commitCommand(args[1]);
                break;
            case "checkout":
                if (args.length < 3 || args.length > 4) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                } else if (args.length == 3) {
                    Repository.checkoutCommand(args[2]);
                } else  {
                    Repository.checkoutCommand(args[1], args[3]);
                }
                break;
            case "log":
                Repository.logCommand();
                break;
            case "global-log":
                Repository.globalLogCommand();
                break;
            case "rm":
                Repository.rmCommand(args[1]);
                break;
            default:
                System.out.println("No command with that name exists.");
                System.exit(0);
                break;
        }
    }
}

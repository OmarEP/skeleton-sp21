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
                if (args.length < 2 || args.length > 4) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                } else if (args.length == 2) {
                    Repository.checkoutCommand(args[1]);
                } else if (args.length == 3) {
                    Repository.checkoutCommand(args[2], args[1]);
                } else  {
                    Repository.checkoutCommand(args[1], args[3], args[2]);
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
            case "find":
                Repository.findCommand(args[1]);
                break;
            case "status":
                Repository.statusCommand();
                break;
            case "branch":
                Repository.branchCommand(args[1]);
                break;
            case "rm-branch":
                Repository.removebranchCommand(args[1]);
                break;
            case "reset":
                Repository.resetCommand(args[1]);
                break;
            default:
                System.out.println("No command with that name exists.");
                System.exit(0);
                break;
        }
    }
}

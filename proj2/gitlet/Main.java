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
        }
    }
}

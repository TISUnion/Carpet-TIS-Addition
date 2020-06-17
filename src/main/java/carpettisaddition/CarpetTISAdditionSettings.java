package carpettisaddition;

import carpet.settings.ParsedRule;
import carpet.settings.Rule;
import carpet.settings.Validator;
import net.minecraft.server.command.ServerCommandSource;

import static carpet.settings.RuleCategory.*;

/**
 * Here is your example Settings class you can plug to use carpetmod /carpet settings command
 */
public class CarpetTISAdditionSettings
{
    public static final String TIS = "TIS";

    public static class ValidatorBlockEventPacketRange extends Validator<Double>
    {
        @Override
        public Double validate(ServerCommandSource source, ParsedRule<Double> currentRule, Double newValue, String string)
        {
            return 0 <= newValue && newValue <= 1024 ? newValue : null;
        }

        @Override
        public String description() { return "You must choose a value from 0 to 1024";}
    }

    @Rule(
            desc = "Set the range where player will receive a block event packet after a block event fires successfully",
            extra = "For piston the packet is used to render the piston movement animation. Decrease it to reduce client's lag",
            validate = ValidatorBlockEventPacketRange.class,
            options = {"0", "16", "64", "128"},
            strict = false,
            category = {TIS, OPTIMIZATION}
    )
    public static double blockEventPacketRange = 64;

}

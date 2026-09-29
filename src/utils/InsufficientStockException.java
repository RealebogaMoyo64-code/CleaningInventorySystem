package utils;

/**
 * Thrown when a stock issuance is attempted for more units than are currently available.
 *
 * Kept as a checked exception so the calling UI code is forced to handle the
 * "not enough stock" case explicitly, rather than treating it as a generic failure.
 *
 * @author Sean
 */
public class InsufficientStockException extends Exception
{
    public InsufficientStockException(String message)
    {
        super(message);
    }
} //InsufficientStockException

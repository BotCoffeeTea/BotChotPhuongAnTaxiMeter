class BluetoothPrinter(private val context: Context) {

    private val adapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    private var socket: BluetoothSocket? = null

    private var outputStream: OutputStream? = null

    fun print(invoice: Invoice) {

        if (adapter == null) {

            Toast.makeText(context, "Bluetooth not supported", Toast.LENGTH_SHORT).show()

            return

        }

        if (!adapter.isEnabled) {

            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)

            (context as Activity).startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)

            return

        }

        val pairedDevices = adapter.bondedDevices

        if (pairedDevices.isEmpty()) {

            Toast.makeText(context, "No paired devices found", Toast.LENGTH_SHORT).show()

            return

        }

        val device = pairedDevices.firstOrNull { it.name == PRINTER_NAME }

        if (device == null) {

            Toast.makeText(context, "Printer not found", Toast.LENGTH_SHORT).show()

            return

        }

        try {

            socket = device.createRfcommSocketToServiceRecord(BLUETOOTH_SPP_UUID)

            socket?.connect()

            outputStream = socket?.outputStream

            val data = invoice.toString().toByteArray()

            outputStream?.write(data)

            outputStream?.flush()

            Toast.makeText(context, "Invoice printed", Toast.LENGTH_SHORT).show()

        } catch (e: IOException) {

            e.printStackTrace()

            Toast.makeText(context, "Failed to connect to printer", Toast.LENGTH_SHORT).show()

        } finally {

            close()

        }

    }

    private fun close() {

        try {

            outputStream?.close()

            socket?.close()

        } catch (e: IOException) {

            e.printStackTrace()

        }

    }

    companion object {

        private const val PRINTER_NAME = "YOUR_PRINTER_NAME"

        private val BLUETOOTH_SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

        private const val REQUEST_ENABLE_BLUETOOTH = 1

    }

}

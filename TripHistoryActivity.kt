class TripHistoryActivity : AppCompatActivity() {

    private lateinit var tripDao: TripDao

    private lateinit var tripList: List<Trip>

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_trip_history)

        tripDao = AppDatabase.getInstance(this).tripDao()

        tripList = tripDao.getTrips()

        tripRecyclerView.layoutManager = LinearLayoutManager(this)

        tripRecyclerView.adapter = TripAdapter(tripList)

        deleteAllButton.setOnClickListener {

            tripList.forEach {

                tripDao.deleteTrip(it)

            }

            tripList = emptyList()

            tripRecyclerView.adapter = TripAdapter(tripList)

        }

    }

    inner class TripAdapter(private val trips: List<Trip>) :

        RecyclerView.Adapter<TripAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

            val view = layoutInflater.inflate(R.layout.item_trip, parent, false)

            return ViewHolder(view)

        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val trip = trips[position]

            holder.dateTextView.text = DateFormat.getDateInstance().format(trip.date)

            holder.distanceTextView.text = getString(R.string.distance, trip.distance)

            holder.fareTextView.text = getString(R.string.fare, trip.fare)

            holder.waitingTimeTextView.text = getString(R.string.waiting_time, trip.waitingTime)

            holder.deleteButton.setOnClickListener {

                tripDao.deleteTrip(trip)

                tripList = tripList.filter { it.id != trip.id }

                notifyDataSetChanged()

            }

        }

        override fun getItemCount(): Int {

            return trips.size

        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)

            val distanceTextView: TextView = itemView.findViewById(R.id.distanceTextView)

            val fareTextView: TextView = itemView.findViewById(R.id.fareTextView)

            val waitingTimeTextView: TextView = itemView.findViewById(R.id.waitingTimeTextView)

            val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)

        }

    }

}

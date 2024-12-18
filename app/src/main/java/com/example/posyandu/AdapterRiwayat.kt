import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.posyandu.R
import com.example.posyandu.dataRiwayat

class AdapterRiwayat(private val antrianList: List<dataRiwayat>) :
    RecyclerView.Adapter<AdapterRiwayat.AntrianViewHolder>() {

    class AntrianViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTanggal: TextView = itemView.findViewById(R.id.tvTanggal)
        val tvNamaLengkap: TextView = itemView.findViewById(R.id.tvNamaLengkap)
        val tvKode: TextView = itemView.findViewById(R.id.tvKode)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AntrianViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fetch_riwayat, parent, false)
        return AntrianViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AntrianViewHolder, position: Int) {
        val antrian = antrianList[position]
        holder.tvTanggal.text = antrian.tanggal
        holder.tvNamaLengkap.text = antrian.namaLengkap
        holder.tvKode.text = antrian.kode


    }


        override fun getItemCount(): Int {
        return antrianList.size
    }
}

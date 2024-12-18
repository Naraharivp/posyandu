import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.posyandu.CekKuota
import com.example.posyandu.R
import com.example.posyandu.dataLayanan

class AdapterHome(val context: Context, val layananList: List<dataLayanan>, val onItemClick: (Int, String) -> Unit) : RecyclerView.Adapter<AdapterHome.LayananViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LayananViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fetch_layanan, parent, false)
        return LayananViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LayananViewHolder, position: Int) {
        val currentItem = layananList[position]

        holder.buttonLayanan.text = currentItem.nama_poli_tsd
        holder.deskripsiTextView.text = currentItem.deskripsi

        holder.cardView.setOnClickListener {
            onItemClick(currentItem.id, currentItem.nama_poli_tsd)
        }

        holder.buttonCek.setOnClickListener {
            // Simpan ID_LAYANAN ke SharedPreferences
            val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putInt("ID_LAYANAN", currentItem.id)
            editor.apply()

            // Buka activity CekKuota
            val intent = Intent(context, CekKuota::class.java)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = layananList.size

    inner class LayananViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.cardViewLayanan)
        val buttonLayanan: TextView = itemView.findViewById(R.id.buttonLayanan)
        val deskripsiTextView: TextView = itemView.findViewById(R.id.deskripsiTextView)
        val buttonCek: Button = itemView.findViewById(R.id.cekAntrianButton)

        // Tambahkan TextView untuk item lainnya jika diperlukan
    }
}

package com.replayx.app.ui

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.replayx.app.R
import com.replayx.app.util.ShizukuHelper
import kotlinx.coroutines.*
import rikka.shizuku.Shizuku
import java.text.SimpleDateFormat
import java.util.*

class OtimizacaoActivity : AppCompatActivity() {

    private lateinit var tvLog: TextView
    private lateinit var scrollLog: ScrollView
    private lateinit var btnOtimizar: Button
    private lateinit var btnReset: Button
    private lateinit var btnVoltar: Button
    private lateinit var tvStatus: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvContador: TextView
    private val SHIZUKU_CODE = 2001
    private val permissionListener = Shizuku.OnRequestPermissionResultListener { requestCode, grantResult ->
        if (requestCode == SHIZUKU_CODE) {
            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                log("[SYS] SHIZUKU_PERMISSAO_CONCEDIDA — toque no botão novamente", isSystem = true)
            } else {
                log("[ERR] SHIZUKU_PERMISSAO_NEGADA", isFail = true)
            }
        }
    }

    private val OPT_COMMANDS = listOf(
        // ── FPS máximo ──
        "cmd power set-fixed-performance-mode-enabled true",
        "settings put system peak_refresh_rate 120.0",
        "settings put system min_refresh_rate 120.0",
        "settings put secure game_auto_temperature_control 0",
        // ── Animações ──
        "settings put global window_animation_scale 0.0",
        "settings put global transition_animation_scale 0.0",
        "settings put global animator_duration_scale 0.0",
        "settings put global disable_window_blurs 1",
        "settings put global accessibility_reduce_transparency 1",
        // ── Touch & Input ──
        "settings put secure long_press_timeout 250",
        "settings put secure multi_press_timeout 250",
        "settings put secure tap_duration_threshold 0",
        "settings put secure touch_blocking_period 0",
        "settings put secure pointer_speed 7",
        "settings put global windowsmgr.max_events_per_sec 150",
        "settings put global view.scroll_friction 0.005",
        "settings put global touch.pressure.scale 0.001",
        // ── GPU & Render ──
        "settings put global debug.egl.hw 1",
        "settings put global debug.egl.profiler 1",
        "settings put global debug.sf.hw 1",
        "settings put global debug.sf.latch_unsignaled 1",
        "settings put global debug.composition.type gpu",
        "settings put global debug.gr.num_framebuffer_surface_buffers 3",
        "settings put global debug.performance.profile 1",
        "settings put global debug.sf.showfps 0",
        "settings put global persist.sys.ui.hw 1",
        "settings put global persist.sys.use_dithering 0",
        "settings put global persist.sys.purgeable_assets 1",
        "settings put global persist.sys.scrollingcache 3",
        "settings put global ro.config.low_ram false",
        // ── Rede & Serviços ──
        "settings put global wifi_sleep_policy 2",
        "settings put global low_power_mode 0",
        "settings put global auto_time 0",
        "settings put global auto_time_zone 0",
        "settings put global bluetooth_on 0",
        "settings put global adaptive_low_power_setting 0",
        "settings put global wifi_scan_always_enabled 0",
        "settings put global ble_scan_always_enabled 0",
        "settings put global location_mode 0",
        "settings put global persist.service.pcsync.enable 0",
        "settings put global persist.service.lgospd.enable 0",
        "settings put global activity_starts_logging_enabled 0",
        "settings put global send_security_reports 0",
        // ── Samsung GOS ──
        "settings put secure gamesdk_version 0",
        "settings put secure game_home_enable 0",
        // ── RAM & CPU ──
        "settings put global zram_enabled 0",
        "settings put global activity_manager_constants max_cached_processes=10",
        "settings put system multicore_packet_scheduler 1",
        "device_config put runtime_native_boot profilebootclasspath true",
        "device_config put runtime_native_boot use_app_image_startup_cache true",
        "am kill-all",
        "pm trim-caches 128G",
        // ── Compilação Free Fire ──
        "cmd package compile -m speed com.dts.freefireth",
        "cmd package compile -m speed com.dts.freefiremax"
    )

    private val RESET_COMMANDS = listOf(
        "settings put global window_animation_scale 1.0",
        "settings put global transition_animation_scale 1.0",
        "settings put global animator_duration_scale 1.0",
        "settings put system peak_refresh_rate 60.0",
        "settings put system min_refresh_rate 60.0",
        "settings put global disable_window_blurs 0",
        "settings put global accessibility_reduce_transparency 0",
        "settings put secure long_press_timeout 500",
        "settings put secure multi_press_timeout 500",
        "settings put secure tap_duration_threshold 150",
        "settings put secure touch_blocking_period 100",
        "settings put secure pointer_speed 0",
        "settings put global windowsmgr.max_events_per_sec 90",
        "settings put global view.scroll_friction 0.01",
        "settings put global touch.pressure.scale 0.003",
        "settings put global debug.egl.hw 0",
        "settings put global debug.egl.profiler 0",
        "settings put global debug.sf.hw 0",
        "settings put global debug.sf.latch_unsignaled 0",
        "settings put global debug.composition.type auto",
        "settings put global debug.gr.num_framebuffer_surface_buffers 2",
        "settings put global debug.performance.profile 0",
        "settings put global persist.sys.ui.hw 0",
        "settings put global persist.sys.use_dithering 1",
        "settings put global persist.sys.purgeable_assets 0",
        "settings put global persist.sys.scrollingcache 1",
        "settings put global ro.config.low_ram true",
        "settings put global wifi_sleep_policy 0",
        "settings put global low_power_mode 1",
        "settings put global auto_time 1",
        "settings put global auto_time_zone 1",
        "settings put global bluetooth_on 1",
        "settings put global adaptive_low_power_setting 1",
        "settings put global wifi_scan_always_enabled 1",
        "settings put global ble_scan_always_enabled 1",
        "settings put global location_mode 3",
        "settings put global persist.service.pcsync.enable 1",
        "settings put global persist.service.lgospd.enable 1",
        "settings put global activity_starts_logging_enabled 1",
        "settings put global send_security_reports 1",
        "settings put secure gamesdk_version 1",
        "settings put secure game_home_enable 1",
        "settings put secure game_auto_temperature_control 1",
        "settings put global zram_enabled 1",
        "settings put global activity_manager_constants max_cached_processes=32",
        "settings put system multicore_packet_scheduler 0",
        "cmd power set-fixed-performance-mode-enabled false",
        "cmd package compile --reset com.dts.freefireth",
        "cmd package compile --reset com.dts.freefiremax",
        "pm trim-caches"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otimizacao)

        tvLog = findViewById(R.id.tvOtimLog)
        scrollLog = findViewById(R.id.scrollOtimLog)
        btnOtimizar = findViewById(R.id.btnAplicarOtimizacao)
        btnReset = findViewById(R.id.btnResetOtimizacao)
        btnVoltar = findViewById(R.id.btnOtimVoltar)
        tvStatus = findViewById(R.id.tvOtimStatus)
        progressBar = findViewById(R.id.progressOtimizacao)
        tvContador = findViewById(R.id.tvContador)

        btnVoltar.setOnClickListener { finish() }

        Shizuku.addRequestPermissionResultListener(permissionListener)

        btnOtimizar.setOnClickListener {
            if (checkShizuku()) runCommands(OPT_COMMANDS, isOptimize = true)
        }

        btnReset.setOnClickListener {
            if (checkShizuku()) runCommands(RESET_COMMANDS, isOptimize = false)
        }

        log("root@devwill:~/ otimizador_ff", isSystem = true)
        log("[SYS] Pronto. Selecione uma ação abaixo.", isSystem = true)
    }

    override fun onDestroy() {
        super.onDestroy()
        Shizuku.removeRequestPermissionResultListener(permissionListener)
    }

    private fun checkShizuku(): Boolean {
        return try {
            if (!Shizuku.pingBinder()) {
                log("[ERR] SHIZUKU_NAO_ATIVO", isFail = true)
                AlertDialog.Builder(this)
                    .setTitle("Shizuku necessário")
                    .setMessage("O Shizuku não está ativo.\n\n1. Abra o app Shizuku\n2. Ative o serviço\n3. Volte aqui e tente de novo")
                    .setPositiveButton("Abrir Shizuku") { _, _ ->
                        try {
                            val intent = packageManager.getLaunchIntentForPackage("moe.shizuku.privileged.api")
                            if (intent != null) startActivity(intent)
                            else log("[ERR] SHIZUKU_NAO_INSTALADO", isFail = true)
                        } catch (e: Exception) {
                            log("[ERR] Instale o Shizuku primeiro", isFail = true)
                        }
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
                false
            } else if (Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
                log("[SYS] SHIZUKU_SOLICITANDO_PERMISSAO", isSystem = true)
                Shizuku.requestPermission(SHIZUKU_CODE)
                false
            } else true
        } catch (ex: Exception) {
            log("[ERR] SHIZUKU: " + ex.message.orEmpty(), isFail = true)
            false
        }
    }

    private fun runCommands(commands: List<String>, isOptimize: Boolean) {
        val label = if (isOptimize) "OTIMIZAÇÃO" else "RESET"
        lifecycleScope.launch {
            setBusy(true)
            tvContador.visibility = View.VISIBLE
            progressBar.max = commands.size
            progressBar.progress = 0
            log("--------------------------------", isSystem = true)
            log("[SYS] >> INICIANDO $label... (${commands.size} cmds)", isSystem = true)
            log("--------------------------------", isSystem = true)

            var ok = 0
            var fail = 0

            commands.forEachIndexed { i, cmd ->
                val shortCmd = if (cmd.length > 48) cmd.take(48) + "…" else cmd
                log("[CMD] $shortCmd")

                val result = withContext(Dispatchers.IO) { ShizukuHelper.run(cmd) }
                val success = !result.contains("ERR", ignoreCase = true) &&
                              !result.contains("FAIL", ignoreCase = true) &&
                              !result.contains("Exception", ignoreCase = true)
                if (success) {
                    log("      ✔ OK", isOk = true)
                    ok++
                } else {
                    val r = if (result.length > 50) result.take(50) + "…" else result
                    log("      ✘ $r", isFail = true)
                    fail++
                }

                withContext(Dispatchers.Main) {
                    progressBar.progress = i + 1
                    tvContador.text = "${i + 1}/${commands.size}"
                }

                delay(30L)
            }

            log("--------------------------------", isSystem = true)
            val icone = if (isOptimize) "▲" else "↺"
            log("$icone $label CONCLUÍDO! ✔ $ok  ✘ $fail", isSystem = true)
            log(">>> REINICIE O DISPOSITIVO para aplicar todas as mudanças.", isSystem = true)
            log("--------------------------------", isSystem = true)

            tvContador.visibility = View.GONE
            setBusy(false)

            tvStatus.text = if (fail == 0) "✔ $label COMPLETO" else "⚠ $label COM $fail ERROS"
            tvStatus.setTextColor(if (fail == 0) 0xFF00E676.toInt() else 0xFFFFD700.toInt())
        }
    }

    private fun setBusy(busy: Boolean) {
        btnOtimizar.isEnabled = !busy
        btnReset.isEnabled = !busy
        // btnVoltar continua sempre habilitado: o usuário pode sair a
        // qualquer momento, mesmo com comandos em execução.
        progressBar.visibility = if (busy) View.VISIBLE else View.INVISIBLE
        if (busy) tvStatus.text = "EXECUTANDO..."
    }

    private fun log(msg: String, isSystem: Boolean = false, isOk: Boolean = false, isFail: Boolean = false) {
        val t = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        val line = if (isSystem) msg else "[$t] $msg"
        val cur = tvLog.text.toString()
        val next = if (cur.isEmpty()) line else "$cur\n$line"
        tvLog.text = next

        // colorir a última linha (workaround simples: usamos SpannableString completo)
        val color = when {
            isOk -> 0xFF00E676.toInt()
            isFail -> 0xFFFF5252.toInt()
            isSystem -> 0xFFFFD700.toInt()
            else -> 0xFFCCCCCC.toInt()
        }
        tvLog.setTextColor(0xFFCCCCCC.toInt()) // base; a coloração por linha requer Spannable
        scrollLog.post { scrollLog.fullScroll(android.view.View.FOCUS_DOWN) }
    }
}

using UnityEngine;
using System.Collections;
using UnityEngine.Networking;

public class Player_SyncPosition : NetworkBehaviour {

    [SyncVar]
    private Vector3 syncPos;

    [SerializeField]
    Transform m_Transform;
    [SerializeField]
    float lerpRate = 15f;
	
	void FixedUpdate () {
        TransmitPosition();
        LerpPosition();
	}

    void LerpPosition()
    {
        if(!isLocalPlayer)
        {
            m_Transform.position = Vector3.Lerp(m_Transform.position, syncPos, Time.deltaTime * lerpRate);
        }
    }

    [Command]
    void CmdProvidePositionToServer(Vector3 pos)
    {
        syncPos = pos;
    }

    [ClientCallback]
    void TransmitPosition()
    {
        if(isLocalPlayer)
        {
            CmdProvidePositionToServer(m_Transform.position);
        }
    }
}

using UnityEngine;
using System.Collections;
using UnityStandardAssets.CrossPlatformInput;
public class Player2dMovementController : MonoBehaviour {
   /* moveForce = basically refers to the force that would be applied on the player when we use the joystick to move him.
    * JumpMultiplier = Refers to the amount the moveForce will be multiplied when we press the jump button
    */
	public float moveForce = 1, JumpMultiplier = 2;
	Rigidbody2D playerBody;
	void Start () 
	{
		playerBody = this.GetComponent<Rigidbody2D>();
	}
	
	void FixedUpdate () 
	{
		Vector2 move_Vector = new Vector2(CrossPlatformInputManager.GetAxis("Horizontal"), CrossPlatformInputManager.GetAxis("Vertical")) * moveForce;
		bool isJumping = CrossPlatformInputManager.GetButton("Jump");

		playerBody.AddForce(move_Vector * (isJumping ? JumpMultiplier : 1));

	}
}
